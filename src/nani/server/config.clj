(ns nani.server.config
  (:require
   [me.raynes.fs :as fs]
   [mount.core :as mount :refer [defstate]]
   [environ.core :refer [env]]
   [taoensso.encore :refer [merge-deep]]
   [fif.core :as fif]))


(def ^:dynamic *default-config-path* "nani.fif")
(def default-config
  {:http-server {:url "127.0.0.1" :port 8080}
   :database
   {:location "resources/nani.db" ;; :memory: for in-memory database
    :schema "resources/sql/schema.sql"
    :force? false
    :migrations []}
   :dev-mode? false})


(defn start
  []
  (println "Retrieving Configuration...")
  (let [config-path (or (env :nani-config-path) *default-config-path*)]
    (if (fs/file? config-path)
      (->> (slurp config-path) fif/reval-string first (merge-deep default-config))
      default-config)))


(defstate config
  :start (start))
