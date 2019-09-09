(ns nani.server.controller.authenticate
  (:require
   [taoensso.timbre :as log]
   [crux.api :as crux]
   [nani.spec]

   [nani.server.db :refer [db]]
   [nani.server.config :refer [config]]
   [nani.server.auth.core :as auth]

   [nani.server.model.user :as model.user]))


(defn- get-password-hash
  [username]
  (some-> 
   (crux/q (crux/db db)
           {:find ['?password-hash]
            :where [['?id :model/type :user]
                    ['?id :user/username username]
                    ['?id :user/password-hash '?password-hash]]})
   first first))


;; (get-password-hash "johndoe3")


(defn is-authorized?
  [username password]
  (let [password-hash (get-password-hash username)]
    (auth/check password password-hash)))


;; (is-authorized? "johndoe3" "test")

