(ns nani.core
  (:require
   [clojure.pprint :refer [pprint]]
   [mount.core :as mount :refer [defstate]]

   ;; Mount Components
   [nani.server.http]
   [nani.server.db]
   [nani.server.logging]
   [nani.server.config :refer [config]]))


(defn -main [& args]
  (println "Initializing Main Server...")
  (mount/start))
