(ns nani.server.fifql.lib.me
  "Contains fifql functionality for logged in sessions."
  (:require
   [cuerdas.core :as str]
   [taoensso.timbre :as log]
   [crux.api :as crux]
   [fifql.core :as fifql]
   [fif.stack-machine :as fif.stack]
   [nani.spec]

   [nani.server.db :refer [db]]
   [nani.server.model.user :as model.user]
   [nani.server.model.discussion :as model.discussion]
   [nani.server.model.post :as model.post]
   [nani.server.model.vote :as model.vote]
   [nani.server.model.comment :as model.comment]))


(def ^:private doc-create-discussion! "
( opts -- ) Create a discussion board

# Keyword Arguments

opts - A map of initial user configuration values
  :discussion/name - Name of the discussion board
  :discussion/user-privileges - Map of users with discussion board privileges
")


(defn create-discussion!
  [sm]
  (let [{user-id :user/id username :user/username} (:request/session sm)
        [discussion-document] (fif.stack/get-stack sm)
        discussion-document 
        (-> discussion-document
            (select-keys [:discussion/name :discussion/user-privileges])
            (assoc :user/username username))]
    (model.discussion/new! discussion-document)
    (-> sm
        fif.stack/pop-stack
        fif.stack/dequeue-code)))



(defn import-nani-me-libs [sm]
  (-> sm

      (fifql/set-word 'me.discussion/create! #'create-discussion!
       :doc (str/trim doc-create-discussion! "\n")
       :group :nani.me.discussion)))
