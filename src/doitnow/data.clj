;; Mock Data Source & Functions
;;
(ns doitnow.data
  (:require [clojure.string :as str]
            [clj-time.core :as time])
  (:import (java.util UUID)))

(def doits
  "Empty map reference for use as a dummy datastore"
  (ref {}))

(defn new-uuid
  "Generates a new UUID string"
  []
  (.toString (UUID/randomUUID)))

(defn uuid?
  "Returns true if the string is a UUID: 36 characters, 0-9 & a-f with -'s"
  [uuid]
  (and
    (not (nil? uuid))
    (string? uuid)
    (re-matches #"[0-9a-f\-]{36}" uuid)))

(defn create-doit
  "Create a new DoIt"
  [doit]
  (let [id (new-uuid)]
    (dosync
      (alter doits assoc id (merge doit {:created (time/now)})))
    id))

(defn query-doits
  "Return a sequence of DoIts"
  []
  (map #(merge (val %) {:id (key %)}) @doits))

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
      (alter doits assoc id 
        (merge (@doits id) (dissoc doit :id) (dissoc doit :created) {:modified (time/now)}))) id)))

(defn delete-doit
  "Delete a DoIt"
  [id]
  (dosync
    (alter doits dissoc id)))