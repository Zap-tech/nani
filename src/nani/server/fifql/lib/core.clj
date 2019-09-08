(ns nani.server.fifql.lib.core
  (:require
   [fif.core :as fif]
   [fif.stack-machine :as fif.stack]
   [fifql.core :as fifql]

   [nani.server.model.user :as model.user]
   [nani.server.controller.authenticate :refer [is-authorized?]]))


(def server-name "Nani - A Quick and Minimal Social Platform")
(def group-name :nani.core)


(defn s-login!
  "Stack operation to prepare stack machine for logging in."
  [sm]
  (let [[password username] (fif/get-stack sm)]
    (-> sm
        (fifql/set-var 'login? true)
        (fifql/set-var 'username username)
        (fifql/set-var 'password password)
        fif.stack/pop-stack
        fif.stack/pop-stack
        fif.stack/dequeue-code)))


(defn s-logout!
  "Stack operation to prepare stack machine for logging out."
  [sm]
  (-> sm
      (fifql/set-var 'logout? true)
      fif.stack/dequeue-code))


(defn handle-login
  "Post Response Handler for logging in through fifql."
  [sm request response]
  (let [login? (fifql/get-var sm 'login?)
        username (fifql/get-var sm 'username)
        password (fifql/get-var sm 'password)]
    (if (true? login?)
      (cond
        (not (string? username)) (throw (ex-info "server/login! - Username must be a string" {:username username}))
        (not (string? password)) (throw (ex-info "server/login! - Password must be a string" {}))
        :else
        (-> response
            (assoc-in [:session :user/id] (model.user/id username))
            (assoc-in [:session :user/username] username)))
      response)))


(defn handle-logout
  "Post Response Handler for logging out through fifql."
  [sm request response]
  (let [logout? (fifql/get-var sm 'logout?)]
    (if (true? logout?)
      (-> response
          (assoc-in [:session :user/id] nil)
          (assoc-in [:session :user/username] nil))
      response)))


(defn import-nani-core-libs [sm]
  (-> sm

      (fifql/set-var
       'server/name server-name
       :doc "Contains the name of the server"
       :group group-name)

      (fifql/set-word 'server/login! s-login!
       :doc "( username password -- ) Login to your current user
       session."
       :group group-name)

      (fifql/set-word 'server/logout! s-logout!
       :doc "( -- ) Logout of the current user session."
       :group group-name)))

