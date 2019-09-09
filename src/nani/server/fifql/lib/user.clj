(ns nani.server.fifql.lib.user
  (:require
   [cuerdas.core :as str]
   [taoensso.timbre :as log]
   [crux.api :as crux]
   [fifql.core :as fifql]
   [nani.spec]

   [nani.server.db :refer [db]]
   [nani.server.model.user :as model.user]))


(def ^:private doc-list-users "
( opts -- user-listing ) List usernames in a vector

# Keyword Arguments

opts - A map of options
  :limit - The number of users to include in the listing for pagination [default: 25]
  :offset - The offset of the listing for pagination [default: 0]
  :order-by - Method of sorting the user listing [default: :user/username]

# Returns

A Vector containing the list of usernames as specified by the provided
options.
")


(defn list-users
  [{:keys [limit offset order-by]
    :or {limit 25 offset 0 order-by :user/username}}]
  (some->>
   (crux/q (crux/db db)
           {:find '[?username]
            :where '[[?id :model/type :user]
                     [?id :user/username ?username]]
            :order-by [['?username :asc]]
            :limit limit
            :offset offset})
   (mapv first)))


(def ^:private doc-register-user! "
( opts -- ) Register the given username to the given email address.

# Keyword Arguments

opts - A map of initial user configuration values
  :user/username - The prefered username to use
  :user/email - Email address linked to the given account
  :user/password - Alphanumeric password to be used with the given account
  :user/fullname - Full name of the user [optional]
")


(defn register-user!
  [user-document]
  (let [user-document (select-keys user-document [:user/username :user/email :user/password :user/fullname])])
  (model.user/new! user-document))


(def ^:private doc-get-user "
( username -- user-model ) Retrieves user data for the given user

# Keyword Arguments

username - Username of the user

# Returns

User model data in the form of a map structure for the given username
")


(defn get-user [username]
  (if-let [user-model (model.user/get username)]
    (dissoc user-model :user/password-hash :crux.db/id)
    (throw (ex-info "Unable to retrieve user data for given username" {:user/username username}))))


(defn import-nani-user-libs [sm]
  (-> sm

      (fifql/set-word 'user/list (fifql/wrap-function 1 #'list-users)
       :doc (str/trim doc-list-users "\n")
       :group :nani.user)

      (fifql/set-word 'user/register! (fifql/wrap-procedure 1 #'register-user!)
       :doc (str/trim doc-register-user! "\n")
       :group :nani.user)

      (fifql/set-word 'user/get (fifql/wrap-function 1 #'get-user)
       :doc (str/trim doc-get-user "\n")
       :group :nani.user)))
