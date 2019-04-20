(ns nani.core
  (:require
   [re-frame.core :as re]
   [taoensso.timbre :as log]))


(defn ^:export init
  []
  (log/info "Initializing Nani..."))
  ;;(re/dispatch-sync [::init]))
