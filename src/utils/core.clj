(ns utils.core
  (:require [com.stuartsierra.component :as c]
            [bouncer.core :as b]
            [clojure.tools.logging :as l]
            [clojure.string :refer [join]]))

(defmacro with-system
  "Starts and stops a system before evaluating body"
  [system bindings & body]
  `(let [system# (c/start ~system)
         ~bindings system#
         v# (try
             ~@body
             (catch Exception e#
               (c/stop system#)
               (throw e#)))]
     (c/stop system#)
     v#))

(defn to-int
  "Convert string to int"
  [s]
  (if (= java.lang.String (class s))
    (Integer/parseInt s)
    s))
 
(defn valid? [v]
  (not (contains? v :bouncer.core/errors)))

(defn validate [& args]
  (second (apply b/validate args)))

(defmacro while->
  "As long as the predicate holds true, threads
  the expr through each form (via `->`). Once pred returns
  false, the current value of all forms is returned."
  [pred expr & forms]
  (let [g (gensym)
        pstep (fn [step] `(if (~pred ~g) (-> ~g ~step) ~g))]
    `(let [~g ~expr
           ~@(interleave (repeat g) (map pstep forms))]
       ~g)))

(defn- log
  "Underlying log function for the public-facing log API. f should
  be a function that receives multiple strings as inputs."
  [f id t messages]
  (f nil (str t ": [" id "] " (join " " messages))))

(defn info
  [id & messages]
  (log `l/warn id "INFO" messages))

(defn warn
  [id & messages]
  (log `l/warn id "WARN" messages))

(defn error
  [id & messages]
  (log `l/warn id "ERROR" messages))
