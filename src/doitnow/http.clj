;; HTTP Helper Functions
;;
(ns doitnow.http
  (:use compojure.core
        ring.util.response
        [clojure.string :only [upper-case]]))

(defn options
  "Generate a 200 HTTP response with an Allow header containing the provided
  HTTP method names - response for an HTTP OPTIONS request"
  ([] (options #{:options} nil))
  ([allowed] (options allowed nil))
  ([allowed body]
    (->
      (response body)
      (header "Allow" (apply str (interpose ", " (map #(upper-case (name %)) allowed)))))))

(defn method-not-allowed
  "Generate a 405 response with an Allow header containing the provided HTTP method names"
  [allowed]
    (->
      (options allowed)
      (status 405)))

(defn no-content?
  "Check for a nil or empty response and set status to 204 (No Content) with nil body"
  [body]
  (if (or (nil? body) (and (seq? body) (empty? body)))
    (->
      (response nil)
      (status 204))
    (response body)))

(defn not-implemented
  "Return an HTTP 501 (Not Implemented)"
  []
  (->
    (response nil)
    (status 501)))
