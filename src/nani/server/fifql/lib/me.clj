(ns nani.server.fifql.lib.me
  "Contains fifql functionality for logged in sessions."
  (:require
   [cuerdas.core :as str]
   [taoensso.timbre :as log]
   [crux.api :as crux]
   [fifql.server :refer [*context*]]
   [fifql.core :as fifql]
   [fif.stack-machine :as fif.stack]
   [nani.spec]

   [nani.server.db :refer [db]]
   [nani.server.model.user :as model.user]
   [nani.server.model.discussion :as model.discussion]
   [nani.server.model.post :as model.post]
   [nani.server.model.vote :as model.vote]
   [nani.server.model.comment :as model.comment]))


(defn user-id []
  (-> *context* :user/id))


(defn username []
  (-> *context* :user/username))


(defn import-nani-me-libs [sm]
  (-> sm

      (fifql/set-word 'me/id (fifql/wrap-function 0 user-id)
       :doc "( -- user-id ) User ID for the given user session."
       :group :nani.me)

      (fifql/set-word 'me/username (fifql/wrap-function 0 username)
       :doc "( -- username ) Username for the given user session."
       :group :nani.me)))
