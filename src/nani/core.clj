(ns nani.core
  (:require
   [mount.core :as mount :refer [defstate]]

   ;; Mount Components
   [nani.server.http]))


(defn -main [& args]
  (println "Starting!")
  (mount/start))
