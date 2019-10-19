(ns nani.server.fifql.lib.core
  (:require
   [fif.core :as fif]
   [fif.stack-machine :as fif.stack]
   [fifql.core :as fifql]
   [fifql.server :refer [*context*]]

   [nani.server.model.user :as model.user]
   [nani.server.controller.authenticate :refer [is-authorized?]]))


(def server-name "Nani - A Quick and Minimal Social Platform")
(def group-name :nani.core)


(defn login!
  "Stack operation to prepare stack machine for logging in."
  [username password]
  (cond
    (not (string? username))
    (throw (ex-info "server/login! - Username must be a string" {:username username}))

    (not (string? password))
    (throw (ex-info "server/login! - Password must be a string" {}))

    (not (is-authorized? username password))
    (throw (ex-info "server/login! - Incorrect Username or Password" {:username username}))

    :else
    (do
      (println (str "Successfully Logged in as '" username "'"))
      (set! *context* (assoc *context*
                             :user/id (model.user/id username)
                             :user/username username
                             ;; TODO: determine session type from username
                             :user/session-type :user)))))


(defn logout!
  "Stack operation to prepare stack machine for logging out."
  []
  (let [{:keys [:user/id :user/username]} *context*]
    (if id
      (println (str "Successfully Logged out as '" username "'"))
      (println "Already Logged out."))
    (set! *context* nil)))


(defn handle-login-response
  "Persists the session for multiple requests from the same user."
  [sm request response]
  (let [{:keys [:user/id :user/username :user/session-type]} *context*]
    (if id
      (-> response
          (assoc-in [:session :user/id] id)
          (assoc-in [:session :user/username] username)
          (assoc-in [:session :user/session-type] session-type))
      response)))


(defn handle-logout-response
  "Persists logging out of the current user to the user's session."
  [sm request response]
  (let [{:keys [:user/id]} *context*]
    (if id
      response
      (assoc response :session nil))))


(def doc-session-type
  "( -- keyword ) Keyword representing the type of stack machine the session is using.

# Return Value

Returns `:guest`, or `:user`")


(defn session-type []
  (let [session-type (-> *context* :user/session-type)]
    (or session-type :guest)))


(defn import-nani-core-libs [sm]
  (-> sm

      (fifql/set-var
       'server/name server-name
       :doc "Contains the name of the server"
       :group group-name)

      (fifql/set-word 'server/login! (fifql/wrap-procedure 2 login!)
       :doc "( username password -- ) Login to a user session."
       :group group-name)

      (fifql/set-word 'server/logout! (fifql/wrap-procedure 0 logout!)
       :doc "( -- ) Logout of the current user session."
       :group group-name)

      (fifql/set-word 'server/session-type (fifql/wrap-function 0 session-type)
       :doc doc-session-type
       :group group-name)))

