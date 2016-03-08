(defn stringify-var
	  [var]
	  (if (instance? clojure.lang.Keyword var)
		  (str "\"" (name var) "\"")
		  (str (if (instance? clojure.lang.Keyword var)
				   (stringify-json var)
				   var))))
(defn wrap-brace
	  [text]
	  (str "{" text "}"))
				   
(defn stringify-json
	  [json]
	  (wrap-brace(clojure.string/join ", " 
						  (map (fn [[key val]] 
								   (str (stringify-var key) ": " (stringify-var val))) 
							   json))))