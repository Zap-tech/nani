(ns nani.server.model.user
  (:require
   [clojure.java.jdbc :as jdbc]
   [taoensso.timbre :as log]

   [nani.server.db :as db]
   [nani.server.config :refer [config]]
   [nani.server.auth.core :as auth]))


(defn new-user!
  [{:keys [user-name full-name password email]}]
  (let [password-hash (auth/encrypt password)
        q "INSERT INTO NaniUser (user_name, full_name, password_hash, email)
           VALUES (?, ?, ?, ?)"]
    (db/execute! [q user-name full-name password-hash email])))


(defn user-by-username [user-name]
  (-> (db/query {:select [:*]
                 :from [:NaniUser]
                 :where [:= :user_name user-name]})
      first))


;;(new-user! {:user-name "john_doh2" :full-name "john. doh2" :password "test" :email "test2@gmail.com"})
;;(auth/check "test" (-> (user-by-username "john_doh2") :password_hash))

