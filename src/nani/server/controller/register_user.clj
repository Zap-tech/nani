(ns nani.server.controller.register-user
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


(defn register-user!
  [user-document])
