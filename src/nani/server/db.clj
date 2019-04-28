(ns nani.server.db
  (:require
   [me.raynes.fs :as fs]
   [mount.core :as mount :refer [defstate]]
  
   [nani.server.config :refer [config]]))


(defn start []
  (let [db {:classname "org.sqlite.JDBC"
            :subprotocol "sqlite"
            :subname (-> config :database :location)}
        schema (-> config :database :schema)]))


(defstate db
  :start (start))
