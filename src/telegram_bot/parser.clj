(ns telegram-bot.parser
 (:require 
		   [clj-http.client :as client]
		   [net.cgrand.enlive-html :as enlive-html]))
		   
(def kage-url "http://www.fansubs.ru/")

(defn get-kage-page
	  []
	  (client/get kage-url))
	  
(defn parse-url
	  [url]
	  (enlive-html/html-resource (java.io.StringReader.((client/get url) :body))))
	  
(defn element-tag
	  [element]
	  (element :tag))
	  
(defn element-attribute
	  [element]
	  (element :attrs))
	  
(defn element-children
	  [element]
	  (let [[tag attr & children] element]
		   children))
	  
(defn selector
	  [doc sel]
	  (enlive-html/select-nodes* doc sel))
	  
(defn get-by-path
	  [document [head & tail]]
	  (let [tag (element-tag document)
			attributes (element-attribute document)
			children (element-children document)
			se-class (head :class)
			se-tag (head :tag)]
		   (println tag)
		   (println attributes)
		   (println (element-tag children))
		   (if (not head)
			   document
			   (get-by-path (some (fn [child] 
									  (let [el-tag (element-tag child)
											attributes (element-attribute child)
											el-class (attributes :class)]
											(and (or (nil? se-class) (nil? el-class) (.contains el-class se-class)) 
												 (or (nil? se-tag) (nil? el-tag) (= el-tag se-tag)) 
												 child))) 
						    children) tail))))
			   