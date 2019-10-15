(ns nani.server.fifql.handler
  (:require
   [fifql.core :as fifql]
   [fifql.server :refer [create-ring-request-handler]]

   [nani.server.fifql.lib.core :as lib.core]
   [nani.server.fifql.stack-machine :as stack-machine]))


(def fifql-handler
  (create-ring-request-handler
   :prepare-stack-machine
   (fn [request]
     (let [{user-id :user/id username :user/username} (:session request)
           stack-machine (if user-id
                           stack-machine/user-stack-machine
                           stack-machine/guest-stack-machine)]
       (-> stack-machine

           (fifql/set-var
            'me/id user-id
            :doc "( -- user-id ) User ID for the given user session."
            :group :nani.me)

           (fifql/set-var
            'me/username username
            :doc "( -- username ) Username for the given user session."
            :group :nani.me)

           (assoc :request/session (:session request)))))

   :post-response
   (fn [sm request response]
     (as-> response $
       (lib.core/handle-login sm request $)
       (lib.core/handle-logout sm request $)))))
