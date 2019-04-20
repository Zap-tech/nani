(ns nani.dev.user
  (:require
   [mount.core :as mount :refer [defstate]]

   ;; Mount Components
   [nani.server.http :as http]))


(def help-message "
  Nani Development Environment
 
  # Life Cycle Functions
  (start)   -- Start Nani Server
  (stop)    -- Stop Nani Server
  (restart) -- Restart Nani Server
  (help)    -- Show this help message
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


(help)
