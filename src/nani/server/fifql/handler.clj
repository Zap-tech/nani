(ns nani.server.fifql.handler
  (:require
   [fifql.core :as fifql]
   [fifql.server :refer [create-ring-request-handler]]

   [nani.server.fifql.lib.core :as lib.core :refer [import-nani-core-libs]]
   [nani.server.fifql.lib.user :as lib.user :refer [import-nani-user-libs]]
   [nani.server.fifql.lib.discussion :as lib.discussion :refer [import-nani-discussion-libs]]
   [nani.server.fifql.lib.me :as lib.me :refer [import-nani-me-libs]]))


(def guest-stack-machine
  (-> (fifql/create-stack-machine)

      ;; Libraries
      import-nani-core-libs
      import-nani-user-libs
      import-nani-discussion-libs

      (fifql/set-var 'server/session-type :guest
       :doc "( -- keyword ) Keyword representing the type of stack machine the session is using."
       :group :nani.core)))


(def user-stack-machine
  (-> (fifql/create-stack-machine)

      ;; Libraries
      import-nani-core-libs
      import-nani-user-libs
      import-nani-discussion-libs
      import-nani-me-libs

      (fifql/set-var 'server/session-type :user
       :doc "( -- keyword ) Keyword representing the type of stack machine the session is using."
       :group :nani.core)))


(def fifql-handler
  (create-ring-request-handler
   :prepare-stack-machine
   (fn [request]
     (let [{user-id :user/id username :user/username} (:session request)
           stack-machine (if user-id
                           user-stack-machine
                           guest-stack-machine)]
       (-> stack-machine

           (fifql/set-var
            'me/id user-id
            :doc "User ID for the given user session."
            :group :nani.me)

           (fifql/set-var
            'me/username username
            :doc "Username for the given user session."
            :group :nani.me)

           (assoc :request/session (:session request)))))

   :post-response
   (fn [sm request response]
     (as-> response $
       (lib.core/handle-login sm request $)
       (lib.core/handle-logout sm request $)))))
