(ns doitnow.query
  (:use [doitnow.helpers])
  (:require [doitnow.data]
            [clj-time.core :as time]
            [korma.core :as sql]))

(defn created-now
  "Set the created time in a DoIt to the current time"
  [model]
  (assoc model :created_at (time/now)))

(defn updated-now
  "Set the modified time in a DoIt to the current time"
  [model]
  (assoc model :updated_at (time/now)))

(defentity web_sites)

(defn get-websites []
  (sql/select web_sites))

(defn create-website [website]
  (let [new-website (created-now (updated-now website))]
    (sql/insert web_sites
      (sql/values (select-keys new-website [:url :rank :snippet :created_at :updated_at])))))
