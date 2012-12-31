(ns doitnow.test.data
  (:use clojure.test
        doitnow.data)
  (:require [clj-time.core :as time]))

(defn mongo-connection [f]
  (dosync
    (alter data-options assoc :db "doitnow-test"))
  (println "Data Options:" @data-options)
  (f))

(use-fixtures :once mongo-connection)

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

