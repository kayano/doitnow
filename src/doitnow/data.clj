;; MongoDB Interface
;;
(ns doitnow.data
  (:use [monger.core :only [connect! set-db! get-db]]
        [monger.result :only [ok?]]
        [validateur.validation])
  (:require [monger.collection :as collection]
            [monger.util :as util]
            [monger.joda-time]
            [monger.json]
            [clj-time.core :as time]))

(def mongo-options
  {:host "localhost"
   :port 27017
   :db "doitnow"
   :doits-collection "doits"})

(connect! mongo-options)
(set-db! (get-db (mongo-options :db)))

(defn- with-oid
  "Add a new Object ID to a DoIt"
  [doit]
  (assoc doit :_id (util/object-id)))

(defn- created-now
  "Set the created time in a DoIt to the current time"
  [doit]
  (assoc doit :created (time/now)))

(defn- modified-now
  "Set the modified time in a DoIt to the current time"
  [doit]
  (assoc doit :modified (time/now)))

(def doit-validator (validation-set
                     (presence-of :_id)
                     (presence-of :title)
                     (presence-of :created)
                     (presence-of :modified)))

(defn create-doit
  "Insert a DoIt into the database"
  [doit]
  (let [new-doit (created-now (modified-now (with-oid doit)))]
    (if (valid? doit-validator new-doit)
      (if (ok? (collection/insert (mongo-options :doits-collection) new-doit))
        new-doit
        (throw (Exception. "Write Failed")))
      (throw (IllegalArgumentException.)))))
