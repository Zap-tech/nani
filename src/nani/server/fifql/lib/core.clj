(ns nani.server.fifql.lib.core
  (:require
   [fif.core :as fif]
   [fif.stack-machine :as fif.stack]
   [fifql.core :as fifql]

   [nani.server.model.user :as model.user]
   [nani.server.controller.authenticate :refer [is-authorized?]]))


(def server-name "Nani - A Quick and Minimal Social Platform")
(def group-name :nani.core)


(defn login!
  [sm]
  (let [[password username] (fif/get-stack sm)]
    (-> sm
        (fifql/set-var 'login? true)
        (fifql/set-var 'username username)
        (fifql/set-var 'password password)
        fif.stack/pop-stack
        fif.stack/pop-stack
        fif.stack/dequeue-code)))


(defn logout!
  [sm]
  (-> sm
      (fifql/set-var 'logout? true)
      fif.stack/dequeue-code))


(defn handle-login [sm request response]
  (let [login? (fifql/get-var sm 'login?)
        username (fifql/get-var sm 'username)
        password (fifql/get-var sm 'password)]
    (if (true? login?)
      (cond
        (not (string? username)) (throw (ex-info "Username must be a string" {:username username}))
        (not (string? password)) (throw (ex-info "Password must be a string" {}))
        :else
        (-> response
            (assoc-in [:session :user/id] (model.user/id username))
            (assoc-in [:session :user/username] username)))
      response)))


(defn handle-logout [sm request response]
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

      (fifql/set-word 'server/login! login!
       :doc "( username password -- ) Login to your current user session"
       :group group-name)

      (fifql/set-word 'server/logout! logout!
       :doc "( -- ) Logout of the current user session"
       :group group-name)))
       
