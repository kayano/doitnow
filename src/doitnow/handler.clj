;; Main HTTP Request Handler
;;

(ns doitnow.handler
  (:require [clojure.walk :refer [keywordize-keys]]
            [compojure.core :refer [ANY
                                    DELETE
                                    GET
                                    HEAD
                                    OPTIONS
                                    POST
                                    PUT
                                    context
                                    defroutes]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [doitnow.data :refer [create-doit get-doit]]
            [doitnow.http :as http]
            [doitnow.middleware :refer [wrap-exception-handler
                                        wrap-request-logger
                                        wrap-response-logger]]
            [ring.middleware.format-response :refer [wrap-restful-response]]
            [ring.middleware.json :refer [wrap-json-body]]))

(defroutes api-routes
  "Main client API route definitions"
  (context "/api" []
           (OPTIONS "/" []
                    (http/options [:options] {:version "0.3.0-SNAPSHOT"}))
           (ANY "/" []
                (http/method-not-allowed [:options]))
           (context "/doits" []
                    (GET "/" []
                         (http/not-implemented))
                    (GET "/:id" [id]
                         (get-doit id))
                    (HEAD "/:id" [id]
                          (http/not-implemented))
                    (POST "/" [:as req]
                          (let [doit (create-doit (keywordize-keys (req :body)))
                                location (http/url-from req (str (doit :_id)))]
                            (http/created location doit)))
                    (PUT "/:id" [id]
                         (http/not-implemented))
                    (DELETE "/:id" [id]
                            (http/not-implemented))
                    (OPTIONS "/" []
                             (http/options [:options :get :head :put :post :delete]))
                    (ANY "/" []
                         (http/method-not-allowed [:options :get :head :put :post :delete]))))
  (route/not-found "Nothing to see here, move along now"))

(def app
  "Application entry point & handler chain"
  (->
   (handler/api api-routes)
   (wrap-json-body)
   (wrap-request-logger)
   (wrap-exception-handler)
   (wrap-response-logger)
   (wrap-restful-response)))
