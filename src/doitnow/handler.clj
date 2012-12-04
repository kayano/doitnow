(ns doitnow.handler
  (:use compojure.core
        ring.util.response
        doitnow.middleware
        doitnow.data
        [ring.middleware.format-response :only [wrap-restful-response]])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes api-routes
  "Main client API route definitions"
  (context "/api" []
    (OPTIONS "/" []
      (->
        (response {:version "0.3.0-SNAPSHOT"})
        (header "Allow" "OPTIONS")))
    (ANY "/" [] 
      (->
        (response nil)
        (status 405)
        (header "Allow" "OPTIONS")))
    (context "/doits" []
      (GET "/" []
        (query-doits))
      (ANY "/" []
        (->
        (response nil)
        (status 405)
        (header "Allow" "OPTIONS")))))
  (route/not-found "Nothing to see here, move along now"))

(def app
  "Application entry point & handler chain"
  (->
    (handler/api api-routes)
    (wrap-request-logger)
    (wrap-exception-handler)
    (wrap-response-logger)
    (wrap-restful-response)))