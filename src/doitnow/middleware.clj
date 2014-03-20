;; Ring Middleware & Utility Functions
;;
(ns doitnow.middleware
  (:use compojure.core
        ring.util.response
        [clojure.string :only [upper-case]]
        [cheshire.custom :only [JSONable]])
  (:require [clojure.tools.logging :as log]
            [clj-time.core :as time]
            [clj-time.format :as format])
  (:import (com.fasterxml.jackson.core JsonGenerator)))

;;
;; dakrone/cheshire JSON library extensions
;; See https://github.com/dakrone/cheshire
;;

(extend java.lang.Exception
  JSONable
  {:to-json (fn [^Exception e ^JsonGenerator jg]
              (.writeStartObject jg)
              (.writeFieldName jg "exception")
              (.writeString jg (.getName (class e)))
              (.writeFieldName jg "message")
              (.writeString jg (.getMessage e))
              (.writeEndObject jg))})

(extend org.joda.time.DateTime
  JSONable
  {:to-json (fn [^org.joda.time.DateTime dt ^JsonGenerator jg]
              (.writeString jg (format/unparse
                                (format/formatters :date-time-no-ms) dt)))})

;;
;; Middleware Handlers
;;

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
          {status :status body :body} response]
      (if (instance? Exception body)
        (log/warn body remote-addr (upper-case (name request-method)) uri "->" status body)
        (log/debug remote-addr (upper-case (name request-method)) uri "->" status))
      response)))

(defn wrap-exception-handler
  "Ring middleware function to trap any uncaught exceptions and return an appropriate
  status code with the exception instance as the response body"
  [handler]
  (fn [req]
    (try
      (handler req)
      (catch IllegalArgumentException e
        (->
         (response e)
         (status 400)))
      (catch Exception e
        (->
         (response e)
         (status 500))))))
