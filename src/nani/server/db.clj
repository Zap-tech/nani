(ns nani.server.db
  (:require
   [mount.core :as mount :refer [defstate]]
   [taoensso.timbre :as log]
   [crux.api :as crux]

   ;; Mount Components
   [nani.server.logging]
   [nani.server.config :refer [config]])
  (:import (crux.api ICruxAPI)))


(declare start stop)
(defstate db
  :start (start)
  :stop (stop))


(defn start []
  (let [crux-config (-> config :database :crux-config)]
    (log/info "Starting Crux JDBC Database Node... " db)
    (crux/start-jdbc-node crux-config)))


(defn stop []
  (.close db))
