(ns utils.test-test
  (:require [clojure.test :refer :all]
            [utils.test :refer [request]]))

(defn handler [req]
  true)

(deftest request-test
  (is (true? (request {:app {:handler handler}} :get "/my-url"))))
