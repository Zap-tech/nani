(ns nani.server.fifql.lib.post
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
   [nani.server.model.discussion :as model.discussion]
   [nani.server.model.post :as model.post]))


(def ^:private doc-list-posts "
( opts -- post-listing ) List posts for a given discussion board in a vector.

# Keyword Arguments

opts -- A map of options
  :discussion/name - Name of the discussion board to retrieve the posts [default: \"All\"]

  :limit - The number of users to include in the listing for pagination [default: 25]

  :offset - The offset of the listing for pagination [default: 0]

  :order-by - Method of sorting the listing [default: :post/votes]
")


(defn list-posts
  [{:keys [:discussion/name :limit :offset :order-by]
    :or {limit 25
         offset 0
         order-by :post/votes
         name "All"}}]
  (cond
    (and (model.discussion/id name) (not (= name "All")))
    (throw (ex-info "Given Discussion Board does not exist"))
    :else
    (some->>
     (crux/q (crux/db db)
             {:find '[?id]
              :where '[[?id :model/type :post]
                       [?id :discussion/name ?discussion-name]]
              :order-by [['?discussion-name :asc]]
              :limit limit
              :offset offset})
     (map first)
     (mapv #(crux/entity (crux/db db) %)))))


(defn get-post
  [post-id]
  (model.post/get post-id))


(def doc-new-post! "
( post-document -- post-id ) Creates a new post, and returns the
  post-id

# Keyword Arguments

post-document - Post Document containing key-values corresponding to a
  post.

# Required `post-document` Options

`:post/title` - The title to give to the post

`:discussion/name` - The discussion board to post to.

# Optional Arguments

`:post/type` - The type of post. Either `:post-type/text`, or
`:post-type/link` [default: `:post-type/text`]

`:post/text` - The URL link, or the text to display in the
  post [default: \"\"]
")


(defn new-post!
  [post-document]
  (auth/check-privileges :user 'post/new!)
  (let [{user-id :user/id username :user/username} *context*
        {discussion-name :discussion/name post-type :post/type} post-document
        post-document (merge {:post/type :post-type/text
                              :post/text ""}
                             post-document)]
        
    (-> post-document
        (select-keys [:discussion/id
                      :user/id
                      :post/title
                      :post/text
                      :post/type
                      :post/votes])
        (dissoc :discussion/name)
        (assoc :discussion/id (model.discussion/id discussion-name)
               :user/id user-id
               :post/votes 0)
        model.post/new!)))


(defn import-nani-post-libs [sm]
  (-> sm

      (fifql/set-word 'post/list (fifql/wrap-function 1 #'list-posts)
       :doc (str/trim doc-list-posts "\n")
       :group :nani.post)

      (fifql/set-word 'post/get (fifql/wrap-function 1 #'get-post)
       :doc "( post-id -- post-model ) Retrieves the post with the given post id"
       :group :nani.post)

      (fifql/set-word 'post/new! (fifql/wrap-function 1 #'new-post!)
       :doc (str/trim doc-new-post! "\n")
       :group :nani.post)))


(comment
 (binding [*context* {:user/username "benzap"
                      :user/id (model.user/id "benzap")
                      :user/session-type :user}]
   (new-post! {:discussion/name "All2" :post/title "Here is a title"})))
