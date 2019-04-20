(ns nani.server.http
  (:require
   [mount.core :as mount :refer [defstate]]

   ;; High-performance Web Server
   [org.httpkit.server :as httpkit]

   ;; Routing Library
   [compojure.core :refer [GET POST defroutes]]
   [compojure.route :as route]
   [ring.middleware.resource :refer [wrap-resource]]
   [ring.middleware.defaults :refer [wrap-defaults site-defaults]]

   ;; Fifql Handler
   [nani.server.fifql.handler :refer [fifql-handler]]
   [fifiql.core :as fifiql]))


;; Create our routes. The fifql ring handler supports both GET and POST requests
(defroutes app-routes
  (GET "/" req "Hello World!")
  (GET "/fifql" req fifql-handler)
  (POST "/fifql" req fifql-handler)
  (GET "/fifiql" req (fifiql/handle-request
                      {:fifiql-source-path "/js/compiled/nani.js"})))


(def site-config
  (-> site-defaults
      (assoc-in [:security :anti-forgery] false)))


(def app
    (-> app-routes
        (wrap-defaults site-config)))


(defn start []
  (println "Starting server on port: 8080")
  (httpkit/run-server #'app {:port 8080}))


(defstate http
   :start (start)
   :stop (http))
