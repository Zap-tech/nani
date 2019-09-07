(ns nani.spec
  (:require
   [clojure.spec.alpha :as s]
   [cuerdas.core :as str]))


;; User Model
(s/def :user/id uuid?)
(s/def :user/username string?)
(s/def :user/email string?) ;; TODO: email verification
(s/def :user/fullname string?)
(s/def :user/password-hash string?)


;; Discussion Model
(s/def :discussion/id uuid?)
(s/def :discussion/name string?)


;; Post Model
(s/def :post/id uuid?)
(s/def :post/title string?)
(s/def :post/text string?)
(s/def :post/type #(contains? #{:post-type/text :post-type/link} %))
