(defproject doitnow "0.3.0-SNAPSHOT"
  :description "Sample project for blog article: Building A Clojure REST Service"
  :url "https://github.com/3rddog/doitnow"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.3"]
                 [ring-middleware-format "0.2.2"]
                 [ring/ring-json "0.1.2"]
                 [org.clojure/tools.logging "0.2.4"]
                 [log4j/log4j "1.2.17"]
                 [clj-time "0.4.4"]
                 [com.novemberain/monger "1.3.4"]
                 [com.novemberain/validateur "1.2.0"]]
  :plugins [[lein-ring "0.7.5"]
            [lein-kibit "0.0.7"]]
  :ring {:handler doitnow.handler/app}
  :profiles
    {:dev
      {:dependencies [[ring-mock "0.1.3"]]}})
