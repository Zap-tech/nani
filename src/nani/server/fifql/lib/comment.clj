(ns nani.server.fifql.lib.comment
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


(def ^:private doc-list-comments "
( opts -- comment-listing ) List comments for a given reference id in a vector.

# Keyword Arguments

opts -- A map of options
  :comment/reference - The reference id (ex. :post/id, :comment/id for child comments)

  :limit - The number of users to include in the listing for pagination [default: 25]

  :offset - The offset of the listing for pagination [default: 0]

  :order-by - Method of sorting the listing [default: :post/votes]

# Notes

- `:comment/reference` can refer to a post by it's `:post/id`, or refer
  to child comments within the post by their corresponding
  `:comment/id`.
")


(defn list-comments
  [{:keys [:comment/reference :limit :offset :order-by]
    :or {limit 25
         offset 0
         order-by :comment/votes}}]
  (cond
    (not reference)
    (throw (ex-info "Unable to get comment listing without a `:comment/reference`." {}))
    
    :else
    (some->>
     (crux/q (crux/db db)
             {:find '[?id]
              :where '[[?id :model/type :comment]
                       [?id :comment/reference ?comment-reference]
                       [?id :comment/votes ?comment-votes]]
              :args [{'?comment-reference reference}]
              :order-by [['?comment-votes :asc]]
              :limit limit
              :offset offset})
     (map first)
     (mapv #(crux/entity (crux/db db) %)))))


(defn import-nani-comment-libs [sm]
  (-> sm

      (fifql/set-word 'comment/list (fifql/wrap-function 1 #'list-comments)
       :doc (str/trim doc-list-comments "\n")
       :group :nani.comment)))


(comment
 (binding [*context* {:user/username "benzap"
                      :user/id (model.user/id "benzap")
                      :user/session-type :user}]
   (list-post-comments {})))
