(defproject doitnow "0.6.0-SNAPSHOT"
  :description "Sample project for blog article: Building A Clojure REST Service"
  :url "https://github.com/3rddog/doitnow"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]
                 [ring-middleware-format "0.3.2"]
                 [ring/ring-json "0.3.0"]
                 [com.taoensso/timbre "3.1.6"]
                 [clj-time "0.6.0"]
                 [com.novemberain/monger "1.7.0"]
                 [com.novemberain/validateur "1.7.0"]
                 [slingshot "0.10.3"]
                 [org.clojure/java.jdbc "0.3.6"]
                 [postgresql "9.3-1102.jdbc41"]
                 [lobos "1.0.0-beta3"]
                 [korma "0.4.0"]]
  :plugins [[lein-ring "0.9.3"]
            [lein-ancient "0.6.6"]]
  :ring {:handler doitnow.handler/app}
  :profiles
  {:dev
   {:dependencies [[javax.servlet/servlet-api "2.5"]
                   [ring-mock "0.1.5"]]}})
