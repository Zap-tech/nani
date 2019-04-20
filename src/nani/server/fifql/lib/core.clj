(ns nani.server.fifql.lib.core
  (:require
   [fifql.core :as fifql]))


(def server-name "Nani - A Quick and Minimal Social Platform")
(def group-name :nani.core)


(defn import-nani-core-libs [sm]
  (-> sm
   
      (fifql/set-var 
       'server-name server-name
       :doc "Contains the name of the server"
       :group group-name)))
