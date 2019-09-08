(ns nani.spec
  (:require
   [clojure.spec.alpha :as s]
   [cuerdas.core :as str]))

;;
;; Helper Functions
;;

(defn strict-conform
  "Checks the given `value` against the given `spec` for
  conformity. Will throw an Exception containing additional
  information on spec conformity.

  # Example

  ```clojure
  (try 
   (strict-conform str? 2)
   (catch Exception ex
    (let [msg (-> (ex-data ex) :message)]
      (println \"Failed, Reason: \" msg))))
  ```"
  [spec value]
  (if (s/valid? spec value)
    value
    (throw (ex-info
            (str "Failed Strict Spec Conform: " (s/explain-str spec value))
            {:type ::spec-strict-conform
             :message (s/explain-str spec value)}))))


;;
;; Fundamental Specs
;;


(s/def :model/type keyword?)


;; User
(s/def :user/id uuid?)
(s/def :user/username string?)
(s/def :user/email string?) ;; TODO: email verification
(s/def :user/fullname string?)
(s/def :user/password-hash string?)


;; Discussion
(s/def :discussion/id uuid?)
(s/def :discussion/name string?)
(s/def :discussion/user-privileges
  (s/map-of uuid? #{:privilege/owner
                    :privilege/admin
                    :privilege/moderator
                    :privilege/guest}))


;; Post
(s/def :post/id uuid?)
(s/def :post/title string?)
(s/def :post/text string?)
(s/def :post/type #{:post-type/text :post-type/link})
(s/def :post/votes int?)


;; Comment
(s/def :comment/id uuid?)
(s/def :comment/text string?)
(s/def :comment/reference uuid?)
(s/def :comment/type #{:comment-type/post :comment-type/comment})
(s/def :comment/votes int?)


;; Vote
(s/def :vote/id uuid?)
(s/def :vote/value #{:vote-value/upvote :vote-value/downvote :vote-value/neutral})
(s/def :vote/type #{:vote-type/post :vote-type/comment})
(s/def :vote/reference uuid?)


;;
;; Model
;;


(s/def :user/model
  (s/keys :req [:crux.db/id
                :model/type
                :user/id
                :user/username
                :user/fullname
                :user/password-hash
                :user/email]))


(s/def :discussion/model
  (s/keys :req [:crux.db/id
                :model/type
                :discussion/id
                :discussion/name
                :discussion/user-privileges]))


(s/def :post/model
  (s/keys :req [:crux.db/id
                :model/type
                :post/id
                :discussion/id
                :user/id
                :post/title
                :post/text
                :post/type
                :post/votes]))


(s/def :comment/model
  (s/keys :req [:crux.db/id
                :model/type
                :comment/id
                :comment/text
                :comment/reference
                :comment/type
                :comment/votes]))


(s/def :vote/model
  (s/keys :req [:crux.db/id
                :model/type
                :vote/id
                :vote/value
                :vote/type
                :vote/reference]))
