(ns nani.server.model.vote
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
   [nani.server.model.post :as model.post]
   [nani.server.model.vote :as model.vote]))


(defn exists? [vote-id user-id]
  (not-empty
   (crux/q (crux/db db)
           {:find ['?id]
            :where [['?id :vote/id vote-id]
                    ['?id :user/id user-id]
                    ['?id :model/type :vote]]})))


(defn new!
  [vote-document]
  (let [{user-id :user/id
         vote-type :vote/type
         vote-value :vote/value
         vote-reference :vote/reference}
        vote-document]
    (cond
      (not (model.user/exists? user-id))
      (throw (ex-info "Cannot create post vote, given user does not exist" {:user/id user-id}))

      ;; TODO make sure vote reference exists based on vote type

      :else
      (let [vote-id (random-uuid)]
        (crux/submit-tx
         db
         [[:crux.tx/put
           {:crux.db/id vote-id
            :vote/id vote-id
            :vote/type vote-type
            :vote/value vote-value
            :model/type :vote
            :user/id user-id}]])))))


(defn update!
  [vote-document]
  (let [{vote-id :vote/id user-id :user/id} vote-document]
    (cond
      (not (exists? vote-id user-id))
      (throw (ex-info "Unable to update non-existant vote" {:vote/id vote-id}))

      :else
      (crux/submit-tx db [[:crux.tx/put vote-document]]))))


(defn get [vote-id]
  (crux/entity (crux/db db) vote-id))
