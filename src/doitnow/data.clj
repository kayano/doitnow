;; MongoDB Interface
;;

(ns doitnow.data
  (:require [clj-time.core :as time]
            [monger.collection :as collection]
            [monger.core :refer [connect! get-db set-db!]]
            [monger.result :refer [ok?]]
            [monger.util :as util]
            [validateur.validation :refer [presence-of
                                           valid? validation-set]]))

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
