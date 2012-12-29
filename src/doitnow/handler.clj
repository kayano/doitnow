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
      ; Query DoIts
      (GET "/" []
        (http/not-implemented))
      ; Get a DoIt by ID
      (GET "/:id" [id]
        (http/not-implemented))
      ; Get DoIt metadata by ID
      (HEAD "/:id" [id]
        (http/not-implemented))
      ; Create new DoIt
      (POST "/" [:as req]
        (let [created (create-doit {:title "tester"})
              location (str (http/url-from req) (created :_id))]
          (->
            (response created)
            (status 201)
            (header "Location" location))))
      ; Update an existing DoIt (or create a new one)
      (PUT "/:id" [id]
        (http/not-implemented))
      ; Delete an existing DoIt
      (DELETE "/:id" [id]
        (http/not-implemented))
      ; Get operation metadata
      (OPTIONS "/" []
        (http/options [:options :get :head :put :post :delete]))
      ; Default for unimplemented HTTP methods
      (ANY "/" []
        (http/method-not-allowed [:options :get :head :put :post :delete]))))
  (route/not-found "Nothing to see here, move along now"))

(def app
  "Application entry point & handler chain"
  (->
    (handler/api api-routes)
    (wrap-request-logger)
    (wrap-exception-handler)
    (wrap-response-logger)
    (wrap-restful-response)))