(ns nani.dev.generator
  "Used to generate fake users, discussions, posts, comments, votes,
  ..."
  (:require
   [taoensso.timbre :as log]
   [crux.api :as crux]
   [nani.spec]

   [nani.shared.random :as random]

   [nani.server.model.user :as model.user]
   [nani.server.model.discussion :as model.discussion]
   [nani.server.model.post :as model.post]
   [nani.server.model.vote :as model.vote]
   [nani.server.model.comment :as model.comment]))


(def generated-user-names
  #{"benzap" "testuser" "guest" "john_doe"})


(def generated-full-names
  #{"Bobby Shakelford"
    "Phil McCracken"
    "Test User"
    "Michael Scott"
    "Creed"})


(defn generate-users! [num-users]
  (let []))
