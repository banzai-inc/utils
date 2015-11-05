(ns utils.test-test
  (:require [clojure.test :refer :all]
            [utils.test :refer [process]]
            [ring.mock.request :refer [request]]))

(defn handler [req]
  true)

(deftest process-request-test
  (let [system {:app {:handler handler}}]
    (is (true? (process system (request :get "/my-url"))))))
