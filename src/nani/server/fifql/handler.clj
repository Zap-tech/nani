(ns nani.server.fifql.handler
  (:require
   [fifql.core :as fifql]
   [fifql.server :refer [create-ring-request-handler]]

   [nani.server.fifql.lib.core :as lib.core :refer [import-nani-core-libs]]
   [nani.server.fifql.lib.user :as lib.user :refer [import-nani-user-libs]]))


(def guest-stack-machine
  (-> (fifql/create-stack-machine)
      import-nani-core-libs))


(def user-stack-machine
  (-> (fifql/create-stack-machine)
      import-nani-core-libs
      import-nani-user-libs))


(def fifql-handler
  (create-ring-request-handler
   :prepare-stack-machine
   (fn [request]
     (let [request-session (:session request)
           user-id (:user/id request-session)
           username (:user/username request-session)
           stack-machine (if user-id guest-stack-machine user-stack-machine)]

       (-> stack-machine

           (fifql/set-var
            'user/id user-id
            :doc "User ID for the given user session."
            :group :nani.user)

           (fifql/set-var
            'user/username username
            :doc "Username for the given user session."
            :group :nani.user)

           (assoc :request/session (:session request)))))

   :post-response
   (fn [sm request response]
     (as-> response $
       (lib.core/handle-login sm request $)
       (lib.core/handle-logout sm request $)))))
