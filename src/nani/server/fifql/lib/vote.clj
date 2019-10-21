(ns nani.server.fifql.lib.vote
  (:require
   [cuerdas.core :as str]
   [taoensso.timbre :as log]
   [crux.api :as crux]
   [fifql.core :as fifql]
   [fifql.server :refer [*context*]]
   [nani.spec]

   [nani.server.fifql.auth :as auth]
   [nani.server.db :refer [db]]
   [nani.server.model.user :as model.user]
   [nani.server.model.discussion :as model.discussion]
   [nani.server.model.post :as model.post]
   [nani.server.model.comment :as model.comment]))


(def ^:private doc-vote-value "
( vote-reference -- vote-value ) Returns an integer which is the
summation of all votes on the given refered post or comment.

# Keyword Arguments

vote-reference - reference id of the entity to determine accumulated
vote values.
")


(defn accumulate-votes [sum vote-value]
  (case vote-value
    :vote-value/upvote (inc sum)
    :vote-value/neutral sum
    :vote-value/downvote (dec sum)))


(defn vote-value
  [vote-reference]
  (cond
    (not (uuid? vote-reference))
    (throw (ex-info "Given `vote-reference argument is not a uuid." {:vote/reference vote-reference}))
    
    :else
    (some->>
     (crux/q (crux/db db)
             {:find '[?vote-value]
              :where '[[?id :model/type :vote]
                       [?id :vote/reference ?vote-reference]
                       [?id :vote/value ?vote-value]]
              :args [{'?vote-reference vote-reference}]})
     (map first)
     (reduce accumulate-votes 0))))


(defn import-nani-vote-libs [sm]
  (-> sm

      (fifql/set-word 'vote/value (fifql/wrap-function 1 #'vote-value)
       :doc (str/trim doc-vote-value "\n")
       :group :nani.vote)))


(comment
 (binding [*context* {:user/username "benzap"
                      :user/id (model.user/id "benzap")
                      :user/session-type :user}]
   (list-votes {})))
