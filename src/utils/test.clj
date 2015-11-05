(ns utils.test
  (:require [ring.mock.request :as mock :refer [body content-type]]
            [cheshire.core :refer [generate-string]]))

(defn process
  "Receives system and processes the request"
  [{:keys [app]} request]
  ((:handler app) request))

(defn json-request
  "Build a standard JSON request with params"
  [req params]
  (-> (content-type req "application/json")
      (body (generate-string params))))

(defn ^{:deprecated "0.8.0"} request
  "Receives a started system and makes a mock request
  to the handler's url"
  [{:keys [app]} method uri & [params]]
  (let [req (mock/request method uri params)]
    ((:handler app) req)))
