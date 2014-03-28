;; MongoDB Interface
;;

(ns doitnow.data
  (:require [clj-time.core :as time]
            [monger.collection :as collection]
            [monger.core :refer [connect! get-db set-db!]]
            [monger.result :refer [ok?]]
            [monger.util :as util]
            [monger.joda-time]
            [validateur.validation :refer [presence-of
                                           valid? validation-set]]
            [slingshot.slingshot :refer [throw+]])
  (:import org.bson.types.ObjectId))

;;
;; Database Connection Details
;;

(def mongo-options
  {:host "localhost"
   :port 27017
   :db "doitnow"
   :doits-collection "doits"})

(connect! mongo-options)
(set-db! (get-db (mongo-options :db)))

;;
;; Utility Functions
;;

(defn- object-id? [id]
  (and
   (not (nil? id))
   (string? id)
   (re-matches #"[0-9a-f]{24}" id)))

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

(defn- with-id
  ""
  [id operation]
  (if (object-id? id)
    (operation)
    (throw+ {:type ::invalid} "Invalid DoIt ID")))

(defn- with-doit
  ""
  [doit operation]
  (if (valid? doit-validator doit)
    (operation)
    (throw+ {:type ::invalid} "Invalid DoIt")))

;;
;; DB Access Functions
;;

(defn create-doit
  "Insert a DoIt into the database"
  [doit]
  (let [new-doit (created-now (modified-now (with-oid doit)))]
    (with-doit new-doit (fn []
                          (if (ok? (collection/insert (mongo-options :doits-collection) new-doit))
                            new-doit
                            (throw+ {:type ::failed} "Create Failed"))))))

(defn get-doit
  "Fetch a DoIt by ID"
  [id]
  (with-id id (fn []
                (let [doit (collection/find-one-as-map
                            (mongo-options :doits-collection) { :_id (ObjectId. id) })]
                  (if (nil? doit)
                    (throw+ {:type ::not-found} (str id " not found"))
                    doit)))))

(defn delete-doit
  "Delete a DoIt by ID"
  [id]
  (with-id id (fn []
                (if (ok? (collection/remove-by-id
                          (mongo-options :doits-collection) { :_id (ObjectId. id) }))
                  nil
                  (throw+ {:type ::failed} "Delete Failed")))))
