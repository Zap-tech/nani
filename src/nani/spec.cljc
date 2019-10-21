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
   (strict-conform string? 2)
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


(def not-empty? #(not (empty? %)))


;;
;; Fundamental Specs
;;


(s/def :model/type keyword?)


;; User
(s/def :user/id uuid?)
(s/def :user/username (s/and string? #(re-matches #"[a-zA-Z0-9_\-]{2,30}" %)))
(s/def :user/email string?) ;; TODO: email verification
(s/def :user/first-name (s/and string? not-empty?))
(s/def :user/last-name (s/and string? not-empty?))
(s/def :user/password-hash (s/and string? not-empty?))
(s/def :user/verified? boolean?)


;; Discussion
(s/def :discussion/id uuid?)
(s/def :discussion/name (s/and string? #(> (count %) 2)))
(s/def :discussion/user-privileges
  (s/map-of :user/id #{:privilege/owner
                       :privilege/admin
                       :privilege/moderator
                       :privilege/guest}))


;; Post
(s/def :post/id uuid?)
(s/def :post/title (s/and string? not-empty?))
(s/def :post/text string?)
(s/def :post/type #{:post-type/text :post-type/link})


;; Comment
(s/def :comment/id uuid?)
(s/def :comment/text (s/and string? not-empty?))
(s/def :comment/reference uuid?)


;; Vote
(s/def :vote/id uuid?)
(s/def :vote/value #{:vote-value/upvote :vote-value/downvote :vote-value/neutral})
(s/def :vote/reference uuid?)


;;
;; Model
;;


(s/def :user/model
  (s/keys :req [:crux.db/id
                :model/type
                :user/id
                :user/username
                :user/password-hash
                :user/email]
          :opt [:user/fullname
                :user/verified?]))


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
                :post/type]))


(s/def :comment/model
  (s/keys :req [:crux.db/id
                :model/type
                :comment/id
                :comment/text
                :comment/reference]))


(s/def :vote/model
  (s/keys :req [:crux.db/id
                :model/type
                :vote/id
                :vote/value
                :vote/reference]))
