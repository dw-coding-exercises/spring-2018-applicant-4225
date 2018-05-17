(ns my-exercise.search
  (:require [hiccup.page :refer [html5]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [ring.util.codec :as codec]
            [ring.middleware.params :as params]
            [ring-curl.core :as ring-curl]
            [clj-http.client :as client]
            [clojure.string :as str]
            [my-exercise.us-state :as us-state]))

;; Header copied from home.clj file. 
(defn header [_]
  [:head
   [:meta {:charset "UTF-8"}]
   [:meta {:name "viewport"
           :content "width=device-width, initial-scale=1.0, maximum-scale=1.0"}]
   [:title "Find my next election"]
   [:link {:rel "stylesheet" :href "default.css"}]])

;; Get election data based on user inputs. 
(defn election-data [request]
  (client/get 
  	(str/replace 
  		(str/lower-case 
  			(str "https://api.turbovote.org/elections/"
  				"upcoming?district-divisions=ocd-division/country:us/state:" (get (get request :params) :state) 
  				",ocd-division/country:us/state:" (get (get request :params) :state)
  				"/place:" (get (get request :params) :city)))#" " "_")))

;; Copied from home.clj file. Can be used for HTML formatting to display results of HTTP GET request.
(defn display [request]
  [:div {:class "display"}
   [:h1 "Getting started" ]
   [:h2]
   [:p (get (get request :params) :street)]
   [:p (ring-curl/to-curl "api.turbovote.org/elections/upcoming")]
   [:p "Thank you for applying to work at Democracy Works! "
    "This coding exercise is designed to show off your ability to program web applications in Clojure. "
    "You should spend no more than 2 hours on it and then turn it in to us "
    "by running the command " [:code "lein submit"] " and following the instructions it prints out. "
    "While we will be evaluating how much of the project you complete, we know that 2 hours isn't enough time to do a "
    "thorough and complete job on all of it, and we're not expecting you to. We just want to see what you get working "
    "in that amount of time."]
   [:p "It is a server-side web application written in Clojure and using the "
    [:a {:href "https://github.com/ring-clojure/ring"} "Ring"] ", "
    [:a {:href "https://github.com/weavejester/compojure"} "Compojure"] ", and "
    [:a {:href "https://github.com/weavejester/hiccup"} "Hiccup"] " libraries."
    "You should feel free to use other libraries as you see fit."]
   [:p "Right now the form below submits to a missing route in the app. To complete the exercise, do the following:"]
   [:ul
    [:li "Create the missing /search route"]
    [:li "Ingest the incoming form parameters"]
    [:li "Derive a basic set of OCD-IDs from the address (see below for further explanation)"]
    [:li "Retrieve upcoming elections from the Democracy Works election API using those OCD-IDs"]
    [:li "Display any matching elections to the user"]]
   [:p "You will get bonus points for:"
    [:ul
     [:li "Documenting your code"]
     [:li "Adding tests for your code"]
     [:li "Standardizing and/or augmenting the address data to derive more OCD division IDs (e.g. county and "
      "legislative districts)"]
     [:li "Noting additional features or other improvements you would make if you had more time"]]]])


(defn page [request]
  (html5
   (election-data request)
   (header request)
   (display request)))
