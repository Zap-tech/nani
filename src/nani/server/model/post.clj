(ns nani.server.model.post
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
   [nani.server.model.discussion :as model.discussion]))


(defn exists? [id]
  (not-empty
   (crux/q (crux/db db)
           {:find ['?id]
            :where [['?id :post/id id]
                    ['?id :model/type :post]]})))


(defn new!
  [post-document]
  (let [{user-id :user/id
         discussion-id :discussion/id
         post-title :post/title
         post-text :post/text
         post-type :post/type}
        post-document]
    (cond
      (not (model.user/exists? user-id))
      (throw (ex-info "Cannot create post, given user does not exist" {:user/id user-id}))

      (not (model.discussion/exists? discussion-id))
      (throw (ex-info "Cannot create post, given discussion does not exist" {:discussion/id discussion-id}))

      :else
      (let [post-id (random-uuid)]
        (crux/submit-tx
         db
         [[:crux.tx/put
           {:crux.db/id post-id
            :post/id post-id
            :model/type :post
            :user/id user-id
            :discussion/id discussion-id
            :post/title post-title
            :post/text post-text
            :post/type post-type}]])))))


(defn update!
  [post-document]
  (let [{post-id :post/id} post-document]
    (cond
      (not (exists? post-id))
      (throw (ex-info "Unable to update non-existant post" {:post/id post-id}))

      :else
      (crux/submit-tx db [[:crux.tx/put post-document]]))))


(defn get [post-id]
  (crux/entity (crux/db db) post-id))
