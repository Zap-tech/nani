(ns nani.server.fifql.handler
  (:require
   [fifql.core :as fifql]
   [fifql.server :refer [create-ring-request-handler *context*]]

   [nani.server.fifql.lib.core :as lib.core]
   [nani.server.fifql.stack-machine :as stack-machine]))


(def fifql-handler
  (create-ring-request-handler
   :prepare-context
   (fn [request]
     (let [{user-id :user/id username :user/username} (:session request)]
       {:user/session-type (if user-id :user :guest)
        :user/id user-id
        :user/username username}))

   :prepare-stack-machine stack-machine/main-stack-machine

   :post-response
   (fn [sm request response]
     (as-> response $
       (lib.core/handle-login-response sm request $)
       (lib.core/handle-logout-response sm request $)))))
