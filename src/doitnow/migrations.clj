(ns doitnow.migrations
  (:refer-clojure :exclude
                  [alter drop bigint boolean char double float time complement])
  (:use [doitnow.data]
        [doitnow.helpers]
        [lobos [migration :only [defmigration]] core schema]))

(defn run-migrations []
  (binding [lobos.migration/*migrations-namespace* 'doitnow.migrations]
    (migrate)))

(defmigration add-todo-table
  (up [] (create (table :items
                        (integer :id :primary-key :auto-inc)
                        (varchar :title 512))))
  (down [] (drop (table :items))))

(defmigration add-web_sites-table
  (up [] (create
          (tbl :web_sites
            (integer :rank)
            (varchar :url 255)
            (text :snippet))))
  (down [] (drop (table :web_sites))))

(defmigration add-icons-table
  (up [] (create
          (tbl :icons
               (refer-to :web_sites)
               (blob :icon))))
  (down [] (drop (table :icons))))
