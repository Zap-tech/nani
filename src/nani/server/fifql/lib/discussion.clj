(ns nani.server.fifql.lib.discussion
  (:require
   [cuerdas.core :as str]
   [taoensso.timbre :as log]
   [crux.api :as crux]
   [fifql.core :as fifql]
   [fifql.server :refer [*context*]]
   [nani.spec]

   [nani.server.fifql.auth :as auth]
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


(def ^:private doc-create-discussion! "
( opts -- ) Create a discussion board

# Keyword Arguments

opts - A map of initial user configuration values

  :discussion/name - Name of the discussion board

  :discussion/user-privileges - Map of users with discussion board privileges
")


(defn create-discussion!
  [discussion-document]
  (auth/check-privileges :user 'discussion/create!)
  (let [{username :user/username} *context*]
    (model.discussion/new!
     (-> discussion-document
         (select-keys [:discussion/name :discussion/user-privileges])
         (assoc :user/username username)))
    (println (str "Successfully Created Discussion Board '" (:discussion/name discussion-document) "'"))))


;; (model.discussion/new! {:user/username "benzap" :discussion/name "All"})


(defn import-nani-discussion-libs [sm]
  (-> sm

      (fifql/set-word 'discussion/list (fifql/wrap-function 1 #'list-discussions)
       :doc (str/trim doc-list-discussions "\n")
       :group :nani.discussion)

      (fifql/set-word 'discussion/get (fifql/wrap-function 1 #'get-discussion)
       :doc (str/trim doc-get-discussion "\n")
       :group :nani.discussion)

      (fifql/set-word 'discussion/create! (fifql/wrap-procedure 1 #'create-discussion!)
       :doc (str/trim doc-create-discussion! "\n")
       :group :nani.discussion)))
