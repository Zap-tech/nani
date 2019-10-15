(ns nani.server.fifql.stack-machine
  (:require
   [fifql.core :as fifql]

   [nani.server.fifql.lib.core :as lib.core :refer [import-nani-core-libs]]
   [nani.server.fifql.lib.user :as lib.user :refer [import-nani-user-libs]]
   [nani.server.fifql.lib.discussion :as lib.discussion :refer [import-nani-discussion-libs]]
   [nani.server.fifql.lib.me :as lib.me :refer [import-nani-me-libs]]))


(def doc-session-type
  "( -- keyword ) Keyword representing the type of stack machine the session is using.

# Return Value

Returns `:guest`, or `:user`")


(def guest-stack-machine
  (-> (fifql/create-stack-machine)

      ;; Libraries
      import-nani-core-libs
      import-nani-user-libs
      import-nani-discussion-libs

      (fifql/set-var 'server/session-type :guest
       :doc doc-session-type
       :group :nani.core)))


(defn promote-to-user [sm]
  (-> sm

      import-nani-me-libs

      (fifql/set-var 'server/session-type :user
       :doc doc-session-type
       :group :nani.core)))


(def user-stack-machine
  (promote-to-user guest-stack-machine))
