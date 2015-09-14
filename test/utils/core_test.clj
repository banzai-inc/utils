(ns utils.core-test
  (:require [clojure.test :refer :all]
            [utils.core :refer :all]
            [com.stuartsierra.component :refer [system-map] :as c]))

(defrecord TestComponent []
  c/Lifecycle
  (start [this] this)
  (stop [this] this))

(deftest with-system-test
  (let [c (TestComponent.)
        sys (system-map :component c)]
   (with-system sys {:keys [component]}
     (is (= c component)))))

(deftest to-int-test
  (is (= 1 (to-int "1"))))

(deftest valid-test
  (testing "valid"
    (is (valid? {})))
  (testing "not valid"
    (is (not (valid? {:bouncer.core/errors "Yeah"})))))

(deftest while-thread-test
  ;; Not a great test, but whatevs
  (is (while-> true? true))
  (is (not (while-> true? false))))