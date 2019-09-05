(ns nani.server.model.post
  (:require
   [clojure.java.jdbc]
   [taoensso.timbre :as log]

   [nani.server.db :as db]
   [nani.server.config :refer [config]]
   [nani.server.auth.core :as auth]

   [nani.server.model.user :as model.user]))


(defn new-discussion!
  [{:keys [user-name discussion-name]}]
  (let [discussion-query "INSERT INTO Discussion (name) VALUES (?)"
        privilege-query "INSERT INTO DiscussionUserPrivilege (user_id, discussion_id, privilege) VALUES (?, ?, ?)"
        user (model.user/user-by-username user-name)
        user-id (:user_id user)]
    (if user-id
      (let [discussion-id (first (db/execute! [discussion-query discussion-name]))]
        (db/execute! [privilege-query user-id discussion-id "owner"]))
      (log/error "Failed to retrieve user-id for given user"))))


;;(new-discussion! {:user-name "john_doh2" :discussion-name "All"})

  
(defn get-user-privilege
  [user-name discussion-name]
  (let [q "SELECT * FROM DiscussionUserPrivilege
           WHERE
           discussion_id = (
             SELECT discussion_id FROM Discussion WHERE name = ?
           )
           AND
           user_id = (
             SELECT user_id FROM NaniUser WHERE user_name = ?
           )"
        result (db/query [q discussion-name user-name])]
    (first result)))


;;(get-user-privilege "john_doh2" "All")


(defn set-user-privilege!
  [{:keys [:user/user-name]}])
