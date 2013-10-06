(defproject doitnow "0.4.0-SNAPSHOT"
  :description "Sample project for blog article: Building A Clojure REST Service"
  :url "https://github.com/3rddog/doitnow"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5"]
                 [ring-middleware-format "0.3.1"]
                 [ring/ring-json "0.2.0"]
                 [org.clojure/tools.logging "0.2.6"]
                 [log4j/log4j "1.2.17"]
                 [clj-time "0.6.0"]
                 [com.novemberain/monger "1.7.0-beta1"]
                 [com.novemberain/validateur "1.5.0"]]
  :plugins [[lein-ring "0.8.7"]
            [lein-kibit "0.0.8"]]
  :ring {:handler doitnow.handler/app}
  :profiles
    {:dev
      {:dependencies [[ring-mock "0.1.5"]]}})
