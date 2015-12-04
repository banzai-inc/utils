(ns utils.core
  (:require [com.stuartsierra.component :as c]
            [bouncer.core :as b]
            [clojure.tools.logging :as l]
            [clojure.string :refer [join]]
            [base64-clj.core :refer [decode]]
            [clojure.data.csv :refer [read-csv]]
            [clojure.stacktrace :refer [print-stack-trace]]))

(defmacro with-system
  "Starts and stops a system before evaluating body"
  [system bindings & body]
  `(let [system# (c/start ~system)
         ~bindings system#
         v# (try
             ~@body
             (catch Exception e#
               (print-stack-trace e#)
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
  (or (not (map? v))
      (not (contains? v :bouncer.core/errors))))

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

(defn csv-to-records
  "Decodes a string and processes it as a CSV,
  returning a list of records"
  [encoded-csv]
  (let [raw (read-csv (decode encoded-csv))]
    (map (partial zipmap (first raw)) (rest raw))))

(defrecord ComponentStub []
  c/Lifecycle
  (start [this] this)
  (stop [this] this))

(defn component-stub [& v]
  (ComponentStub.))

(def ^:private alphanumeric "ABCDEFGHIJKLMNPQRSTUVWXYZ123456789")

(defn rand-code [length]
  (apply str (repeatedly length #(rand-nth alphanumeric))))

(defn indexed
  "Turns a list of maps into a map, indexed by index-key. Optional
  order? adds an :order key."
  [coll & {:keys [index-key order?]
           :or {index-key :id
                order? false}}]
  (reduce #(let [s (if order?
                     (assoc %2 :order (.indexOf (map index-key coll) (get %2 index-key)))
                     %2)]
             (assoc %1 (get s index-key) s)) {} coll))
 
(defn find-by
  "Finds maps in collection given a key and a value"
  [coll k v]
  (filter #(= (get % k) v) coll))

(defn find-first-by
  "Finds maps in collection given a key and a value"
  [coll k v]
  (first (find-by coll k v)))
