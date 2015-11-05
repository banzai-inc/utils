(ns utils.test-test
  (:require [clojure.test :refer :all]
            [utils.test :refer [process json-request]]
            [ring.mock.request :refer [request]]))

(defn handler [req]
  true)

(deftest process-request-test
  (let [system {:app {:handler handler}}]
    (is (true? (process system (request :get "/my-url"))))))

(deftest json-request-test
  (is (= java.io.ByteArrayInputStream
         (-> (json-request (request :get "/my/uri") {:my "data"})
             (:body)
             (type)))))
