(ns nani.server.model.comment
  (:refer-clojure :exclude [get])
  (:require
   [clojure.java.jdbc]
   [taoensso.timbre :as log]
   [cuerdas.core :as str]
   [crux.api :as crux]

   [nani.spec]
   [nani.server.util.uuid :refer [random-uuid]]
   [nani.server.db :refer [db]]
   [nani.server.config :refer [config]]
   [nani.server.auth.core :as auth]

   [nani.server.model.user :as model.user]
   [nani.server.model.discussion :as model.discussion]
   [nani.server.model.post :as model.post]))


(defn exists? [id]
  (not-empty
   (crux/q (crux/db db)
           {:find ['?id]
            :where [['?id :post/id id]
                    ['?id :model/type :comment]]})))


(defn new!
  [comment-document]
  (let [{user-id :user/id
         comment-text :comment/text
         comment-parent :comment/parent
         comment-children :comment/children}
        comment-document]
    (cond
      (not (model.user/exists? user-id))
      (throw (ex-info "Cannot create post comment, given user does not exist" {:user/id user-id}))

      (not (model.discussion/exists? discussion-id))
      (throw (ex-info "Cannot create post comment, given discussion does not exist" {:discussion/id discussion-id}))

      (not (model.post/exists? post-id))
      (throw (ex-info "Cannot create post comment, given post does not exist" {:post/id post-id}))

      :else
      (let [comment-id (random-uuid)]
        (crux/submit-tx
         db
         [[:crux.tx/put
           {:crux.db/id comment-id
            :comment/id comment-id
            :post/id post-id
            :model/type :comment
            :user/id user-id
            :discussion/id discussion-id
            :comment/text comment-text}]])))))


(defn update!
  [comment-document]
  (let [{comment-id :comment/id} comment-document]
    (cond
      (not (exists? comment-id))
      (throw (ex-info "Unable to update non-existant comment" {:comment/id comment-id}))

      :else
      (crux/submit-tx db [[:crux.tx/put comment-document]]))))


(defn get [comment-id]
  (crux/entity (crux/db db) comment-id))
