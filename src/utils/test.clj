(ns utils.test
  (:require [ring.mock.request :as mock]
            [ring.util.response :refer [content-type]]))

(defn process
  "Receives system and processes the request"
  [{:keys [app]} request]
  ((:handler app) request))

(defn ^{:deprecated "0.8.0"} request
  "Receives a started system and makes a mock request
  to the handler's url"
  [{:keys [app]} method uri & [params]]
  (let [req (mock/request method uri params)]
    ((:handler app) req)))
