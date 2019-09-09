(ns nani.server.fifql.lib.discussion
  (:require
   [cuerdas.core :as str]
   [taoensso.timbre :as log]
   [crux.api :as crux]
   [fifql.core :as fifql]
   [nani.spec]

   [nani.server.db :refer [db]]
   [nani.server.model.user :as model.user]
   [nani.server.model.discussion :as model.discussion]))


(def ^:private doc-list-discussions "
( opts -- discussion-listing ) List discussions in a vector

# Keyword Arguments

opts -- A map of options
  :limit - The number of users to include in the listing for pagination [default: 25]
  :offset - The offset of the listing for pagination [default: 0]
  :order-by - Method of sorting the user listing [default: :discussion/name]
")


(defn list-discussions
  [{:keys [limit offset order-by]
    :or {limit 25 offset 0 order-by :discussion/name}}]
  (some->>
   (crux/q (crux/db db)
           {:find '[?name]
            :where '[[?id :model/type :discussion]
                     [?id :discussion/name ?name]]
            :order-by [['?name :asc]]
            :limit limit
            :offset offset})
   (mapv first)))


(def ^:private doc-get-discussion "
( name -- discussion-model ) Retrieves user data for the given user

# Keyword Arguments

name - Name of the discussion

# Returns

Discussion model data in the form of a map structure for the given discussion name
")


(defn get-discussion [name]
  (if-let [discussion-model (model.discussion/get name)]
    discussion-model
    (throw (ex-info "Unable to retrieve discussion data for given discussion name" {:discussion/name name}))))


(defn import-nani-discussion-libs [sm]
  (-> sm

      (fifql/set-word 'discussion/list (fifql/wrap-function 1 #'list-discussions)
       :doc (str/trim doc-list-discussions "\n")
       :group :nani.discussion)

      (fifql/set-word 'discussion/get (fifql/wrap-function 1 #'get-discussion)
       :doc (str/trim doc-get-discussion "\n")
       :group :nani.discussion)))
