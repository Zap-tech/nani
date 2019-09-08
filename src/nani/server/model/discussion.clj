(ns nani.server.model.discussion
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



(defn exists? [id]
  (not-empty
   (crux/q (crux/db db)
           {:find ['?id]
            :where [['?id :discussion/id id]
                    ['?id :model/type :discussion]]})))


(defn id [name]
  (some-> 
   (crux/q (crux/db db)
           {:find ['?id]
            :where [['?id :discussion/name name]
                    ['?id :model/type :discussion]]})
   first first))


(defn new!
  [{:keys [:user/username :discussion/name] :as discussion-document}]
  (cond
    (not (model.user/id username))
    (throw (ex-info "Cannot create discussion, given user does not exist" {:user/username username}))

    (id name)
    (throw (ex-info "Given discussion already exists" {:discussion/name name}))

    :else
    (let [discussion-id (random-uuid)]
      (crux/submit-tx
       db
       [[:crux.tx/put
         {:crux.db/id discussion-id
          :discussion/id discussion-id
          :model/type :discussion
          :discussion/name name
          :discussion/user-privileges {username :privilege/owner}}]]))))


(defn update!
  [discussion-document]
  (let [{discussion-id :discussion/id discussion-name :discussion/name}
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


(comment
  (new! {:user/username "john_doh2" :discussion/name "all3"})
  (exists? (id "all3"))
  (crux/entity (crux/db db) (id "all3")))

