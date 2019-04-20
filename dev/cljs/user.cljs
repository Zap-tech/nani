(ns cljs.user
  (:require
   [taoensso.timbre :as log]
   [mount.core :as mount :refer [defstate]]

   ;; Fifiql Development
   [fifiql.init]
   
   ;; Mount Points
   [nani.core]))


(enable-console-print!)


(defn start []
  (log/debug "Nani Development Environment Started!")
  (mount/start))


(defn stop []
  (mount/stop))


(defn restart []
  (stop)
  (start))
