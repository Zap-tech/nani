(ns nani.spec
  (:require
   [clojure.spec.alpha :as s]
   [cuerdas.core :as str]))


;; User Model
(s/def :user/user-name string?)
(s/def :user/email string?) ;; TODO: email verification
(s/def :user/full-name string?)
(s/def :user/password-hash string?)
(s/def :user/date-created string?)

;; Discussion Model
(s/def :discussion/name string?)
(s/def :discussion/id pos-int?)
(s/def :discussion/date-created string?)

;; Discussion Privilege
(s/def :discussion-privilege/id pos-int?)
(s/def :discussion-privilege/privilege #(contains? #{:none :moderator :admin :owner}))
