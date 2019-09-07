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

   [nani.server.model.user :as model.user]))


(defn id [name]
  (some-> 
   (crux/q (crux/db db)
           {:find ['?id]
            :where [['?id :discussion/name name]]})
   first first))


(defn new!
  [{:keys [:user/username :discussion/name] :as discussion-document}]
  (cond
    (not (model.user/id username))
    (throw (ex-info "Cannot create discussion, given user does not exist" {:user/username username}))

    (id name)
    (throw (ex-info "Given discussion already exists" {:discussion/name name}))

    :else
    (crux/submit-tx
     db
     [[:crux.tx/put
       {:crux.db/id (random-uuid)
        :discussion/name name
        :discussion/user-privileges {username :owner}}]])))

  
(defn update!
  [discussion-document]
  (let [{discussion-id :crux.db/id discussion-name :discussion/name}
        discussion-document]
    (cond
      (not (id discussion-name))
      (throw (ex-info "Unable to update non-existant dicsussion" {:discussion/name discussion-name}))

      (not= discussion-id (id discussion-name))
      (throw (ex-info "Given discussion document is invalid"
                      {:store-hash (id discussion-name) :invalid-hash discussion-id}))

      :else
      (crux/submit-tx db [[:crux.tx/put discussion-document]]))))


(defn get [discussion-name]
  (when-let [id (id discussion-name)]
    (crux/entity (crux/db db) id)))
