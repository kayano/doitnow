(ns doitnow.test.http
  (:use clojure.test
        ring.mock.request  
        doitnow.http))

(deftest http-options
  (testing "HTTP Options Default Response"
    (let [response (options)]
      (is (= (response :status) 200))
      (is (nil? (response :body)))
      (is (contains? (response :headers) "Allow"))
      (is (= ((response :headers) "Allow") "OPTIONS"))))
  (testing "HTTP Options With-Allowed Response"
    (let [response (options [:get :post])]
      (is (= (response :status) 200))
      (is (nil? (response :body)))
      (is (contains? (response :headers) "Allow"))
      (is (= ((response :headers) "Allow") "GET, POST"))))
  (testing "HTTP Options With-Body Response"
    (let [response (options [:get :post] {:version "version-number"})]
      (is (= (response :status) 200))
      (is (map? (response :body)))
      (is (contains? (response :body) :version))
      (is (contains? (response :headers) "Allow"))
      (is (= ((response :headers) "Allow") "GET, POST")))))

(deftest http-method-not-allowed
  (testing "HTTP Method Not Allowed With-Options"
    (let [response (method-not-allowed [:options :get])]
      (is (= (response :status) 405))
      (is (nil? (response :body)))
      (is (contains? (response :headers) "Allow"))
      (is (= ((response :headers) "Allow") "OPTIONS, GET")))))

(deftest http-no-content?
  (testing "HTTP No-Content nil Response")
  (testing "HTTP No-Content empty Response")
  (testing "HTTP No-Content not-a-sequence Response"))