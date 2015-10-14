(ns utils.test
  (:require [ring.mock.request :as mock]))

(defn request
  "Receives a started system and makes a mock request
  to the handler's url"
  [{:keys [app]} method uri & [params]]
  (let [req (mock/request method uri params)]
    ((:handler app) req)))
