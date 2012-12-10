;; Main HTTP Request Handler
;;
(ns doitnow.handler
  (:use compojure.core
        ring.util.response
        doitnow.middleware
        doitnow.data
        [ring.middleware.format-response :only [wrap-restful-response]])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [doitnow.http :as http]))

(defroutes api-routes
  "Main client API route definitions"
  (context "/api" []
    (OPTIONS "/" []
      (http/options [:options] {:version "0.3.0-SNAPSHOT"}))
    (ANY "/" [] 
      (http/method-not-allowed [:options]))
    (context "/doits" []
      (GET "/" []
        (http/no-content? (query-doits)))
      (OPTIONS "/" []
        (http/options [:options :get]))
      (ANY "/" []
        (http/method-not-allowed [:options :get]))))
  (route/not-found "Nothing to see here, move along now"))

(def app
  "Application entry point & handler chain"
  (->
    (handler/api api-routes)
    (wrap-request-logger)
    (wrap-exception-handler)
    (wrap-response-logger)
    (wrap-restful-response)))