(ns nani.server.logging
  (:require
   [taoensso.timbre :as log]
   [mount.core :as mount :refer [defstate]]

   ;; Mount Components
   [nani.server.config :refer [config]]))


(defstate logging
  :start (log/merge-config! (get config :logging {})))
