(ns nani.server.fifql.lib.user
  (:require
   [cuerdas.core :as str]
   [taoensso.timbre :as log]
   [crux.api :as crux]
   [fifql.core :as fifql]
   [nani.spec]

   [nani.server.db :refer [db]]
   [nani.server.model.user :as model.user]))


(def doc-list-users "
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
  "")


(defn import-nani-user-libs [sm]
  (-> sm

      (fifql/set-word 'user/list (fifql/wrap-function 1 list-users)
       :doc (str/trim doc-list-users "\n")
       :group :nani.user)))
