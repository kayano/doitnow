;; MongoDB Interface
;;
(ns doitnow.data
  (:use [monger.core :only [connect! connect set-db! get-db]]
        [validateur.validation])
  (:require [monger.collection :as collection]
            [monger.conversion :as conversion]
            [monger.result :as result]
            [monger.util :as util]
            [monger.joda-time]
            [monger.json]
            [clj-time.core :as time]))

(def mongo-options (ref
  { :host "localhost" :port 27017 :db "doitnow" :collection "doits" }))

(connect! @mongo-options)
(set-db! (monger.core/get-db (@mongo-options :db)))

(defn- with-oid
  "Add a new Object ID to a DoIt"
  [doit]
  (merge { :_id (util/object-id) } doit))

(defn- created-now
  "Set the created time in a DoIt to the current time"
  [doit]
  (merge { :created (time/now) } doit))

(defn- modified-now
  "Set the modified time in a DoIt to the current time"
  [doit]
  (merge { :modified (time/now) } doit))
 
(def doit-validator (validation-set
                  (presence-of :_id)
                  (presence-of :title)
                  (presence-of :created)
                  (presence-of :modified)))

(defn create-doit
  "Insert a DoIt into the database"
  [doit]
  (let [new-doit (created-now (modified-now (with-oid doit)))
        validation-errors (doit-validator new-doit)]
    (println new-doit)
    (if (empty? validation-errors)
      (do
        (collection/insert (@mongo-options :collection) new-doit)
        ;; need to add success check
        new-doit)
      (throw (IllegalArgumentException.)))))