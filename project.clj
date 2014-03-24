(defproject doitnow "0.5.0-SNAPSHOT"
  :description "Sample project for blog article: Building A Clojure REST Service"
  :url "https://github.com/3rddog/doitnow"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.6"]
                 [ring-middleware-format "0.3.2"]
                 [ring/ring-json "0.3.0"]
                 [com.taoensso/timbre "3.1.6"]
                 [clj-time "0.6.0"]
                 [com.novemberain/monger "1.7.0"]
                 [com.novemberain/validateur "1.7.0"]
                 [slingshot "0.10.3"]]
  :plugins [[lein-ring "0.8.7"]
            [lein-kibit "0.0.8"]]
  :ring {:handler doitnow.handler/app}
  :profiles
  {:dev
   {:dependencies [[ring-mock "0.1.5"]]}})
