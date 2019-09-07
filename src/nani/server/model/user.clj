(ns nani.server.model.user
  (:refer-clojure :exclude [get])
  (:require
   [clojure.java.jdbc :as jdbc]
   [taoensso.timbre :as log]
   [crux.api :as crux]
   [nani.spec]

   [nani.server.util.uuid :refer [random-uuid]]
   [nani.server.db :refer [db]]
   [nani.server.config :refer [config]]
   [nani.server.auth.core :as auth]))


(defn exists? [id]
  (not-empty
   (crux/q (crux/db db)
           {:find ['?id]
            :where [['?id :user/id id]
                    ['?id :type :user]]})))


(defn id [username]
  (some-> 
   (crux/q (crux/db db)
           {:find ['?id]
            :where [['?id :user/username username]
                    ['?id :type :user]]})
   first first))


(defn new!
  [{:keys [:user/username :user/fullname :user/password :user/email]}]
  (if-not (id username)
    (let [password-hash (auth/encrypt password)
          user-id (random-uuid)]
      (crux/submit-tx
       db
       [[:crux.tx/put
         {:crux.db/id user-id
          :user/id user-id
          :type :user
          :user/username username
          :user/fullname fullname
          :user/password-hash password-hash
          :user/email email}]]))
    (throw (ex-info "Given user with the provided username already exists" {:user/username username}))))


(defn update!
  [user-document]
  (let [{user-id :crux.db/id username :user/username} user-document]
    (cond 
      (not (id username))
      (throw (ex-info "Unable to update non-existant user" {:user/username username}))
      
      (not= user-id (id username))
      (throw (ex-info "Given user document is invalid" {:store-hash (id username) :invalid-hash user-id}))

      :else
      (crux/submit-tx db [[:crux.tx/put user-document]]))))


(defn get [username]
  (when-let [id (id username)]
    (crux/entity (crux/db db) id)))


(comment
  (new! {:user/username "john_doh2"
         :user/fullname "john. doh2"
         :user/password "test"
         :user/email "test2@gmail.com"})
  (id "john_doh2")
  (id "john_doh1")
  (get "john_doh2")
  (get "john_doh")
  
  (-> (get "john_doh2")
      (assoc :user/email "john.doe.2@gmail.com")
      update!)

  (get "john_doh2")

  (auth/check "test" (-> (user-by-username "john_doh2") :password_hash)))
