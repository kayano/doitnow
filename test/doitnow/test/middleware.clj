(ns doitnow.test.middleware
  (:use clojure.test
        ring.mock.request  
        doitnow.middleware))

(defn- exception-thrower
  "Private function for throwing an exception while testing handler functions"
  [handler]
  (throw (Exception. "Testing, 123")))

(deftest test-wrap-exception-handler
  (testing "Exception Handling"
    (let [response ((wrap-exception-handler exception-thrower) (request :get "/api"))]
      (is (= (response :status) 500))
      (is (contains? (response :body) :exception))
      (is (contains? (response :body) :message)))))