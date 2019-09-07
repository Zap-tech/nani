(ns nani.server.config
  (:require
   [me.raynes.fs :as fs]
   [mount.core :as mount :refer [defstate]]
   [environ.core :refer [env]]
   [taoensso.encore :refer [merge-deep]]
   [fif.core :as fif]))


(def ^:dynamic *default-config-path* "nani.fif")
(def default-config
  {:environment-name "Default Environment"
   :http-server {:url "127.0.0.1" :port 8080}
   :database
   {:crux-config {:dbtype "sqlite"
                  :dbname "resources/db/nani_crux.db"
                  :db-dir "resources/db/nani_crux_data"}
    :secret "nani"
    :migrations []}
   :dev-mode? false
   :logging {:min-level :warn}})


(defn start
  []
  (println "Retrieving Configuration...")
  (let [config-path (or (env :nani-config-path) *default-config-path*)]
    (if (fs/file? config-path)
      (->> (slurp config-path) fif/reval-string first (merge-deep default-config))
      default-config)))


(defstate config
  :start (start))
