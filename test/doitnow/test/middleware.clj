(ns doitnow.test.middleware
  (:require [clojure.test :refer :all]
            [doitnow.middleware :refer :all]
            [ring.mock.request :refer :all]))

(deftest test-wrap-exception-handler
  (testing "Exception Handling"
    (let [handler (wrap-exception-handler (fn [req] (throw (Exception. "Testing, 123"))))
          response (handler (request :get "/api"))]
      (is (= (response :status) 500))
      (is (instance? Exception (response :body)))
      (is (= (.getMessage (response :body)) "Testing, 123"))))
  (testing "IllegalArgumentException Handling"
    (let [handler (wrap-exception-handler (fn [req] (throw (IllegalArgumentException. "Testing, 123"))))
          response (handler (request :get "/api"))]
      (is (= (response :status) 400))
      (is (instance? IllegalArgumentException (response :body)))
      (is (= (.getMessage (response :body)) "Testing, 123"))))  )