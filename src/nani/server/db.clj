(ns nani.server.db
  (:require
   [clojure.java.jdbc :as jdbc]

   [honeysql.core :as sql]
   [me.raynes.fs :as fs]
   [mount.core :as mount :refer [defstate]]
   [taoensso.timbre :as log]
   [crux.api :as crux]

   ;; Mount Components
   [nani.server.logging]
   [nani.server.config :refer [config]])
  (:import (crux.api ICruxAPI)))


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
       email TEXT NOT NULL UNIQUE,
       date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
     )"}

   {:name "Discussion"
    :creation-query
    "CREATE TABLE Discussion (
       discussion_id INTEGER NOT NULL PRIMARY KEY,
       name TEXT UNIQUE NOT NULL,
       date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
     )"}

   {:name "DiscussionUserPrivilege"
    :creation-query
    "CREATE TABLE DiscussionUserPrivilege (
       privilege_id INTEGER NOT NULL PRIMARY KEY,
       user_id INTEGER NOT NULL,
       discussion_id INTEGER NOT NULL,
       privilege TEXT CHECK( privilege IN ('none', 'moderator', 'admin', 'owner') ) NOT NULL DEFAULT 'none',
       UNIQUE(user_id, discussion_id),
       FOREIGN KEY(discussion_id) REFERENCES Discussion(discussion_id),
       FOREIGN KEY(user_id) REFERENCES NaniUser(user_id)
     )"}

   {:name "Post"
    :creation-query
    "CREATE TABLE Post (
       post_id INTEGER NOT NULL PRIMARY KEY,
       post_title TEXT NOT NULL,
       post_text TEXT,
       post_type TEXT CHECK( post_type IN ('text', 'link') ) NOT NULL DEFAULT 'text',
       discussion_id INTEGER NOT NULL,
       date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       date_edited TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY(discussion_id) REFERENCES Discussion(discussion_id)
     )"}
   
   {:name "PostVote"
    :creation-query
    "CREATE TABLE PostVote (
       vote_id INTEGER NOT NULL PRIMARY KEY,
       user_id INTEGER NOT NULL,
       post_id INTEGER NOT NULL,
       is_upvote BOOLEAN,
       UNIQUE(user_id, post_id),
       FOREIGN KEY(post_id) REFERENCES Post(post_id),
       FOREIGN KEY(user_id) REFERENCES NaniUser(user_id)
     )"}

   {:name "PostComment"
    :creation-query
    "CREATE TABLE PostComment (
       comment_id INTEGER NOT NULL PRIMARY KEY,
       parent_id INTEGER, /* NULL if it doesn't have a parent */
       comment_text TEXT NOT NULL,
       user_id INTEGER NOT NULL,
       post_id INTEGER NOT NULL,
       date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       date_edited TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
       FOREIGN KEY(post_id) REFERENCES Post(post_id),
       FOREIGN KEY(user_id) REFERENCES NaniUser(user_id)
     )"}

   {:name "CommentVote"
    :creation-query
    "CREATE TABLE CommentVote (
       vote_id INTEGER NOT NULL PRIMARY KEY,
       user_id INTEGER NOT NULL,
       comment_id INTEGER NOT NULL,
       is_upvote BOOLEAN,
       UNIQUE(user_id, comment_id),
       FOREIGN KEY(comment_id) REFERENCES PostComment(comment_id),
       FOREIGN KEY(user_id) REFERENCES NaniUser(user_id)
     )"}])


(defn start []
  (let [crux-config (-> config :database :crux-config)]
    (log/info "Starting Crux JDBC Database Node... " db)
    (crux/start-jdbc-node crux-config)))


(defn stop []
  (.close db))


(comment

  (def ^crux.api.ICruxAPI node
    (crux/start-jdbc-node {:dbtype "sqlite"
                           :dbname "nani-crux.db"
                           :db-dir "resources/db/nani-crux"}))

  (crux/submit-tx
   node
   [[:crux.tx/put
     {:crux.db/id :test
      :user/name "Test Username"
      :user/email "benzap@tbaytel.net"}]])

  (crux/submit-tx
   node
   [[:crux.tx/put
     {:crux.db/id :test
      :user/name "Test Username"
      :user/email2 "benzap@tbaytel.net"}]])

  (crux/q (crux/db node)
          '{:find [?e]
            :where [[?e :user/name "Test Username"]]})

  (crux/entity (crux/db node) :test)

  (crux/submit-tx
   node
   [[:crux.tx/put
     {:crux.db/id :benzap
      :user/username "benzap"
      :user/email "benzap@tbaytel.net"}]]))
