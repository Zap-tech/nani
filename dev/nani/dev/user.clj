(ns nani.dev.user
  (:require
   [clojure.pprint :refer [pprint]]
   [mount.core :as mount :refer [defstate]]
   [crux.api :as crux]

   ;; Mount Components
   [nani.server.http :refer [http]]
   [nani.server.config :refer [config]]
   [nani.server.db :refer [db]]
   [nani.server.logging :refer [logging]]))


(def help-message "
  Nani Development Environment
 
  # Life Cycle Functions
  (start)         -- Start Nani Server
  (stop)          -- Stop Nani Server
  (restart)       -- Restart Nani Server

  # Misc.
  (print-config)  -- Print the Configuration
  (help)          -- Show this help message
  ")


(defn start []
  (mount/start))


(defn stop []
  (mount/stop))


(defn restart []
  (stop)
  (start))


(defn help []
  (println help-message))


(defn print-config []
  (pprint config))


(defn -main [& args]
  (println "Initializing Dev Server...")
  (start))
