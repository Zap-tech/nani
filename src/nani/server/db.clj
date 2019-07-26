(ns nani.server.db
  (:require
   [clojure.java.jdbc :as jdbc]

   [honeysql.core :as sql]
   [me.raynes.fs :as fs]
   [mount.core :as mount :refer [defstate]]
   [taoensso.timbre :as log]

   ;; Mount Components
   [nani.server.logging]
   [nani.server.config :refer [config]]))


(declare start stop)
(defstate db
  :start (start)
  :stop (stop))


(def schema
  [{:name "NaniUser"
    :creation-query
    "CREATE TABLE NaniUser (
       user_id INTEGER NOT NULL PRIMARY KEY,
       user_name TEXT NOT NULL UNIQUE,
       full_name TEXT,
       password_hash TEXT NOT NULL,
       email TEXT NOT NULL UNIQUE
     )"}

   {:name "Discussion"
    :creation-query
    "CREATE TABLE Discussion (
       discussion_id INTEGER NOT NULL PRIMARY KEY,
       name TEXT NOT NULL
     )"}

   {:name "Post"
    :creation-query
    "CREATE TABLE Post (
       post_id INTEGER NOT NULL PRIMARY KEY,
       post_title TEXT NOT NULL,
       post_text TEXT,
       post_type TEXT CHECK( post_type IN ('text', 'link') ) NOT NULL DEFAULT 'text',
       discussion_id INTEGER NOT NULL,
       FOREIGN KEY(discussion_id) REFERENCES Discussion(discussion_id)
     )"}
   
   {:name "PostComment"
    :creation-query
    "CREATE TABLE PostComment (
       comment_id INTEGER NOT NULL PRIMARY KEY,
       parent_id INTEGER, /* NULL if it doesn't have a parent */
       comment_text TEXT NOT NULL,
       user_id INTEGER NOT NULL,
       post_id INTEGER NOT NULL,
       FOREIGN KEY(post_id) REFERENCES Post(post_id),
       FOREIGN KEY(user_id) REFERENCES NaniUser(user_id)
     )"}])


(defn list-tables
  "Lists all of the tables currently in the sqlite3 database."
  [db]
  (let [q "SELECT name FROM sqlite_master WHERE type = 'table'"
        results (jdbc/query db q)]
    (if (empty? results)
      #{}
      (->> results (map :name) set))))


;; (list-tables db)


(defn has-table? [db name]
  (contains? (list-tables db) name))


;; (has-table? "NaniUser")


(defn create-tables! [db]
  (log/info "Creating Database Tables...")
  (doseq [{:keys [name creation-query]} schema]
    (if-not (has-table? db name)
      (do
        (log/info "- Creating Table " name "...")
        (jdbc/execute! db creation-query))
      (log/info "- Table " name " already exists, skipping..."))))


(defn drop-tables! [db]
  (doseq [{:keys [name creation-query]} (reverse schema)]
    (when (has-table? db name)
      (log/info "- Dropping Table " name "...")
      (jdbc/execute! db (str "DROP TABLE " name)))))


(defn delete-database! []
  (if-let [dbpath (-> config :database :location)]
    (when (fs/file? dbpath)
      (log/info "Deleting database: " dbpath "...")
      (fs/delete dbpath))
    (log/info "Attempted to delete database, but it does not exist.")))


(defn start []
  (let [db {:classname "org.sqlite.JDBC"
            :subprotocol "sqlite"
            :subname (-> config :database :location)}]
    (let [conn (jdbc/get-connection db)
          db (assoc db :connection conn)]
      (log/info "Starting Database... " db)
      (create-tables! db)
      db)))


(defn stop []
  (let [dev-mode? (-> config :dev-mode?)]
    (when dev-mode?
      (log/debug "In dev-mode, dropping tables...")
      (drop-tables! db))
    (when-let [conn (:connection db)]
      (.close conn))))


;;
;; Functions used with the mounted database
;;


(defn query [q]
  (if (map? q)
    (jdbc/query db (sql/format q))
    (jdbc/query db q)))


(defn execute! [q]
  (if (map? q)
    (jdbc/execute! db (sql/format q))
    (jdbc/execute! db q)))


(defn insert! [& args]
  (apply jdbc/insert! db args))


(defn insert-multi! [& args]
  (apply jdbc/insert-multi! db args))


(defn update! [& args]
  (apply jdbc/update! db args))


(defn delete! [& args]
  (apply jdbc/delete! db args))
