(ns doitnow.test.data
  (:use clojure.test
        doitnow.data)
  (:require [clj-time.core :as time]))

;(defn data-fixture [f]
;  (dosync
;    (alter doits assoc (new-uuid)
;      {:title "Test Doit #1"
;       :description "A test DoIt"
;       :created (time/now) 
;       :due (time/plus (time/now) (time/days 2))})
;    (alter doits assoc (new-uuid)
;      {:title "Test Doit #2"
;       :description "A test DoIt" 
;       :created (time/now) 
;       :due (time/plus (time/now) (time/months 3))}))
;  (f))
;
;(use-fixtures :once data-fixture)

(defn- object-id? [id]
  (and
    (not (nil? id))
    (string? id)
    (re-matches #"[0-9a-f]{24}" id)))

(deftest test-create-doit
  (testing "Create Valid DoIt"
    (let [doit {:title "Newly Created Test DoIt"
                :description "A test DoIt" 
                :due (time/plus (time/now) (time/weeks 2))
                :priority 1}
          created (create-doit doit)]
      (is (map? created))
      (is (contains? created :_id))))
  (testing "Create Invalid DoIt"
    (is (thrown? IllegalArgumentException (create-doit {})))))

