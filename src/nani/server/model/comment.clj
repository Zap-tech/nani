(ns nani.server.model.comment
  (:refer-clojure :exclude [get])
  (:require
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
  (let [{user-id :user/id} comment-document]
    (cond
      (not (model.user/exists? user-id))
      (throw (ex-info "Cannot create post comment, given user does not exist" {:user/id user-id}))

      :else
      (let [comment-id (random-uuid)
            comment-model
            (merge
             comment-document
             {:crux.db/id comment-id
              :comment/id comment-id
              :model/type :comment})]

        (crux/submit-tx db [[:crux.tx/put (nani.spec/strict-conform :comment/model comment-model)]])))))


(defn update!
  [comment-model]
  (let [{comment-id :comment/id} comment-model]
    (cond
      (not (exists? comment-id))
      (throw (ex-info "Unable to update non-existant comment" {:comment/id comment-id}))

      :else
      (crux/submit-tx db [[:crux.tx/put (nani.spec/strict-conform :comment/model comment-model)]]))))


(defn get [comment-id]
  (crux/entity (crux/db db) comment-id))
