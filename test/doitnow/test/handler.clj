(ns doitnow.test.handler
  (:require [clojure.test :refer :all]
            [doitnow.handler :refer :all]
            [ring.mock.request :refer :all]))

(deftest test-api-routes
  (testing "API Options"
    (let [response (api-routes (request :options "/api"))]
      (is (= (response :status) 200))
      (is (contains? (response :body) :version))))
  (testing "API Get"
    (let [response (api-routes (request :get "/api"))]
      (is (= (response :status) 405))
      (is (nil? (response :body)))))
  (testing "Not Found"
    (let [response (api-routes (request :get "/invalid"))]
      (is (= (response :status) 404)))))

(deftest test-create-doit
  (testing "Create valid doit"
    (let [response (api-routes
                      (-> (request :post "/api/doits")
                          (assoc :body {:title "Test DoIt"})))
          response-body (response :body)
          response-headers (response :headers)]
      (is (= (response :status) 201))
      (is (contains? response-headers "location"))
      (is (map? response-body))
      (is (contains? response-body :_id))
      (is (contains? response-body :title))
      (is (contains? response-body :created))
      (is (contains? response-body :modified)))))
