(ns doitnow.middleware
  (:use compojure.core
        ring.util.response
        [clojure.string :only [upper-case]])
  (:require [clojure.tools.logging :as log]))

(defn wrap-request-logger
  "Ring middleware function that uses clojure.tools.logging to write a debug message
  containing remote address, request method & URI of incoming request"
  [handler]
  (fn [req]
    (let [{remote-addr :remote-addr request-method :request-method uri :uri} req]
      (log/debug remote-addr (upper-case (name request-method)) uri)
      (handler req))))

(defn wrap-response-logger
  "Ring middleware function that uses clojure.tools.logging to write a debug message
  containing remote address, request method, URI & response status of outgoing response"
  [handler]
  (fn [req]
    (let [response (handler req)
          {remote-addr :remote-addr request-method :request-method uri :uri} req
          {status :status} response]
      (log/debug remote-addr (upper-case (name request-method)) uri "->" status)
      response)))

(defn wrap-exception-handler
  "Ring middleware function to trap any uncaught exceptions and return a standard 500 response"
  [handler]
  (fn [req]
    (try
      (handler req)
      (catch Exception e
        (let [{remote-addr :remote-addr request-method :request-method uri :uri} req]
          (log/warn e remote-addr (upper-case (name request-method)) uri "-> 500 " e)
          (->
            (response {:exception (.getName (class e)) :message (.getMessage e)})
            (status 500)))))))