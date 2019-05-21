(ns nani.server.model.user
  (:require
   [clojure.java.jdbc :as jdbc]
   [nani.server.db :as db]
   [taoensso.timbre :as log]
   [nani.server.config :refer [config]]))


(defn new-user!
  [{:keys [user-name full-name password-hash email]}]
  (let [q "INSERT INTO NaniUser (user_name, full_name, password_hash, email)
           VALUES (?, ?, ?, ?)"]
    (db/execute! [q user-name full-name password-hash email])))


;;(new-user! {:user-name "john_doh2" :full-name "john. doh2" :password-hash "test" :email "test2@gmail.com"})


(defn user-by-username [user-name]
  (-> (db/query {:select [:*]
                 :from [:NaniUser]
                 :where [:= :user_name user-name]})
      first))
      

;;(user-by-username "john_doh2")
