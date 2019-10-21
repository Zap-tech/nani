(ns nani.server.model.post
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
   [nani.server.model.discussion :as model.discussion]))


(defn exists? [id]
  (when (uuid? id)
    (not-empty
     (crux/q (crux/db db)
             {:find ['?id]
              :where [['?id :post/id id]
                      ['?id :model/type :post]]}))))


(def discussion-blacklist
  #{"All" "Random"})


(defn new!
  [post-document]
  (log/debug "Creating new post..." post-document)
  (let [{user-id :user/id discussion-id :discussion/id} post-document
        discussion-name (some-> (crux/entity (crux/db db) discussion-id) :discussion/name)]
    (cond
      (not (model.user/exists? user-id))
      (throw (ex-info "Cannot create post, given user does not exist" {:user/id user-id}))

      (not (model.discussion/exists? discussion-id))
      (throw (ex-info "Cannot create post, given discussion does not exist" {:discussion/id discussion-id}))

      (contains? discussion-blacklist discussion-name)
      (throw (ex-info "Cannot create posts for pseudo-discussion boards" {:discussion/blacklist discussion-blacklist}))

      :else
      (let [post-id (random-uuid)
            post-model
            (merge
             post-document
             {:crux.db/id post-id
              :post/id post-id
              :model/type :post})]
        (crux/submit-tx db [[:crux.tx/put (nani.spec/strict-conform :post/model post-model)]])
        post-id))))


(defn update!
  [post-model]
  (let [{post-id :post/id} post-model]
    (cond
      (not (exists? post-id))
      (throw (ex-info "Unable to update non-existant post" {:post/id post-id}))

      :else
      (crux/submit-tx db [[:crux.tx/put (nani.spec/strict-conform :post/model post-model)]]))))


(defn get [post-id]
  (crux/entity (crux/db db) post-id))
