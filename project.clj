(defproject telegram-bot "0.1.0-SNAPSHOT"
  :description "Telegram bot app"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
				 [compojure "1.4.0"]
				 [ring/ring-jetty-adapter "1.4.0"]
				 [cheshire "5.5.0"]
                 [environ "1.0.0"]
				 [clj-http "2.1.0"]
				 [enlive "1.1.6"]]
  ;:min-lein-version "2.0.0"
  ;:plugins [[environ/environ.lein "0.3.1"]]
  ;:hooks [environ.leiningen.hooks]
  ;:uberjar-name "telegram-bot-standalone.jar"
  ;:profiles {:production {:env {:production true}}})
  :main ^:skip-aot telegram-bot.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
