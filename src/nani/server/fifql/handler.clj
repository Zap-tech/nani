(ns nani.server.fifql.handler
  (:require
   [fifql.core :as fifql]
   [fifql.server :refer [create-ring-request-handler]]

   [nani.server.fifql.lib.core :refer [import-nani-core-libs]]))


(def guest-stack-machine
  (-> (fifql/create-stack-machine)
      import-nani-core-libs))


(def fifql-handler
   (create-ring-request-handler
    :prepare-stack-machine guest-stack-machine))
