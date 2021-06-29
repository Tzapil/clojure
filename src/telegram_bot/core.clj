(ns telegram-bot.core
  (:use [telegram-bot.parser])
  (:require [compojure.core :refer [defroutes GET PUT POST DELETE ANY]]
			[clojure.walk :as walk]
			[clj-http.client :as client]
            [compojure.handler :refer [site]]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [ring.adapter.jetty :as jetty]
            [environ.core :refer [env]]
			[cheshire.core :refer :all])	;; json-work
  (:gen-class))

(def bot-token "")
(def bot-url (str "https://api.telegram.org/bot" bot-token "/"))
(def my-url "")

(def chat-id 53941045)	;; default chat id
	   
(def base-json {
		:content-type :json
		:socket-timeout 1000  ;; in milliseconds
		:conn-timeout 1000    ;; in milliseconds
		:accept :json
	})

(defn get-bot
	  [method]
	  (client/get (str bot-url method)))

(defn post-bot
	  ([method]
	   (post-bot method {}))
	  ([method json]
		(println (str "Post BOT Method: " method))
		(println (str "Json params:\n" json))
	    (client/post (str bot-url method) (assoc base-json :form-params json))))
	   
(defn get-updates 
	  []
	  (get-bot "getUpdates"))
	
(defn set-bot-hook
      [url]
	  (let [answer (post-bot "setWebhook" {:url url})]
		   (if (= (answer :status))
			   (println "It's ok creating web-hook!")
			   (println "Error creating web-hook!"))
			   answer))

(defn drop-bot-hook
	  []
	  (drop-bot-hook)
	  (set-bot-hook ""))
			   
(defn send-text-message
	  ([message]
	   (send-text-message chat-id message))
	  ([chat message]
	   (post-bot "sendMessage" {
								:chat_id chat 
								:text message
							  })))
(defn init
	  []
	  (set-bot-hook (str my-url "hook")))
	
(defn hook-message [req]
  (println (str "New bot msg: \n" req))
  (let [body (parse-string (slurp (req :body)) true)
		username (or (get-in body [:message :from :username]) (get-in body [:message :from :first_name]))
		userid (get-in body [:message :from :id])]
	    (println (str "Body: \n" body))
	    (println (str "Id: " userid))
		(println (str "Name: " username))
	    (if (and body (body :update_id))
			(send-text-message userid (str "I'm get your message, " username "!!"))))
	{:status 200})

(defroutes app
  (POST "/hook" req
       (println "HOOK_MSG") "HELLO BRO") ;; (hook-message req)
  (ANY "*" []
       (route/not-found (slurp (io/resource "404.html")))))

(defn -main [& [port]]
  (let [port (Integer. (or port (env :port) 80))]
	(println (str "Running on port:" port))
	(init)
    (jetty/run-jetty (site #'app) {:port port :join? false})))