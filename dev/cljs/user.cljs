(ns cljs.user
  (:require
   [taoensso.timbre :as log :include-macros true]

   ;; Fifiql Development
   [fifiql.init]
   
   ;; Mount Points
   [nani.core]))


(log/merge-config!
 {:min-level nil})


(def development-config
  {:name "Development"})
   


(defn start []
  (enable-console-print!)
  (log/info "Nani Development Environment Started!")
  (nani.core/start (merge nani.core/production-config development-config)))
