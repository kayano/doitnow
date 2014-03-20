(ns doitnow.test.data
  (:require [clj-time.core :as time]
            [clojure.test :refer :all]
            [doitnow.data :refer :all]
            [monger.core :refer [connect! set-db!]]))

(defn mongo-connection [f]
  (connect! { :host "localhost" :port 27017 })
  (set-db! (monger.core/get-db "doitnow-test"))
  (f))

(use-fixtures :once mongo-connection)

(defn- object-id? [id]
  (and
    (not (nil? id))''
    (string? id)
    (re-matches #"[0-9a-f]{24}" id)))

(deftest test-create-doit
  (testing "Create Valid DoIt"
    (let [doit {:title "Newly Created Test DoIt"
                :description "A New Test DoIt" 
                :due (time/plus (time/now) (time/weeks 2))
                :priority 1}
          created (create-doit doit)]
      (is (map? created))
      (is (contains? created :_id))
      (is (contains? created :title))
      (is (contains? created :description))
      (is (contains? created :due))
      (is (contains? created :priority))
      (is (contains? created :created))
      (is (contains? created :modified))))
  (testing "Create Invalid DoIt"
    (is (thrown? IllegalArgumentException (create-doit {})))))

