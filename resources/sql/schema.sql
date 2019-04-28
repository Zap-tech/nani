/* Main SQLite Schema for Nani */

CREATE TABLE user(
       user_id INT NOT NULL INTEGER PRIMARY KEY,
       user_name TEXT NOT NULL,
       full_name TEXT,
       email TEXT
);


CREATE TABLE discussion(
       discussion_id NOT NULL INTEGER PRIMARY KEY,
       name TEXT NOT NULL
);


CREATE TABLE post(
       post_id NOT NULL INTEGER PRIMARY KEY,
       post_title TEXT NOT NULL,
       post_text TEXT,
       post_type TEXT CHECK( post_type IN ('text', 'link') ) NOT NULL DEFAULT 'text'
       discussion_id NOT NULL INTEGER,
       FOREIGN KEY(discussion_id) REFERENCES discussion(discussion_id)
);


CREATE TABLE post_comment(
       comment_id NOT NULL INTEGER PRIMARY KEY,
       parent_id INTEGER, /* NULL if it doesn't have a parent */
       comment_text TEXT NOT NULL,
       post_id NOT NULL INTEGER,
       FOREIGN KEY(post_id) REFERENCES post(post_id)
);


