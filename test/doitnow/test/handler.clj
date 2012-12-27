(ns doitnow.test.handler
  (:use clojure.test
        ring.mock.request  
        doitnow.handler))

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

;(deftest test-doit-routes
;  (testing "List All DoIts"
;    (let [response (api-routes (request :get "/api/doits"))
;          doits (response :body)]
;      (is (= (response :status) 200))
;      (is (seq doits))
;      (is (every? map? doits))
;      (is (every? #(contains? % :id) doits))
;      (is (every? #(contains? % :title) doits))
;      (is (every? #(contains? % :created) doits)))))