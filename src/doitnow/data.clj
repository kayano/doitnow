(ns doitnow.data
  (:require [clojure.string :as str]
            [clj-time.core :as time])
  (:import (java.util UUID)))

(defn new-uuid []
  (str/replace (str/upper-case (.toString (UUID/randomUUID))) "-" ""))

(def doits 
  (ref 
    (-> {}
      (assoc (new-uuid)
        {:title "Test Doit #1" :created (time/now) :due (time/plus (time/now) (time/days 2))})
      (assoc (new-uuid)
        {:title "Test DoIt #2" :created (time/now) :due (time/plus (time/now) (time/months 3))}))))

(defn create-doit
  "Create a new DoIt"
  [doit]
  (let [id (new-uuid)]
    (dosync
      (alter doits assoc id (merge doit {:created (time/now)})))
    id))

(defn query-doits
  "Return a list of DoIts"
  []
  (vals @doits))

(defn get-doit
  "Get a DoIt by ID"
  [id]
  (if (contains? @doits id)
    (assoc (doits id) :id id)
    nil))

(defn update-doit
  "Update a DoIt by ID"
  [id doit]
  (if (contains? @doits id)
    ((dosync
      (alter doits assoc id (merge (@doits id) doit {:updated (time/now)}))) id)))

(defn delete-doit
  "Delete a DoIt"
  [id]
  (dosync
    (alter doits dissoc id)))