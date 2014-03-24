(ns doitnow.test.middleware
  (:require [clojure.test :refer :all]
            [doitnow.middleware :refer :all]
            [ring.mock.request :refer :all]
            [slingshot.slingshot :refer [throw+]]))

(deftest test-wrap-exception-handler
  (testing "General Exception Handling"
    (let [handler (wrap-exception-handler (fn [req] (throw+ (Exception. "Server Error"))))
          response (handler (request :get "/api"))]
      (is (= (response :status) 500))))
  (testing "Operation Failed Handling"
    (let [handler (wrap-exception-handler (fn [req] (throw+ {:type :doitnow.data/failed} "500: Failed")))
          response (handler (request :get "/api"))]
      (is (= (response :status) 500))))
  (testing "Invalid Handling"
    (let [handler (wrap-exception-handler (fn [req] (throw+ {:type :doitnow.data/invalid} "400: Bad Request")))
          response (handler (request :get "/api"))]
      (is (= (response :status) 400))))
  (testing "Not Found Handling"
    (let [handler (wrap-exception-handler (fn [req] (throw+ {:type :doitnow.data/not-found} "404: Not Found")))
          response (handler (request :get "/api"))]
      (is (= (response :status) 404)))))
