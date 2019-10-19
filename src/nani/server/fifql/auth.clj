(ns nani.server.fifql.auth
  (:require
   [fifql.server]))


(defn is-guest-session? []
  (let [{:keys [:user/session-type]} fifql.server/*context*]
    (= session-type :guest)))


(defn is-user-session? []
  (let [{:keys [:user/session-type]} fifql.server/*context*]
    (= session-type :user)))


(defn is-moderator-session? []
  (let [{:keys [:user/session-type]} fifql.server/*context*]
    (= session-type :moderator)))


(defn is-admin-session? []
  (let [{:keys [:user/session-type]} fifql.server/*context*]
    (= session-type :admin)))


(defn is-owner-session? []
  (let [{:keys [:user/session-type]} fifql.server/*context*]
    (= session-type :owner)))


(defn check-privileges [required-session-type & [fname]]
  (let [{:keys [:user/session-type]} fifql.server/*context*]
    (case required-session-type
      :guest nil

      :user
      (when-not (contains? #{:user :moderator :admin :owner} session-type)
        (ex-info "Given session does not have the proper privileges to call this word function."
                 {:error-type ::not-privileged
                  :fname fname
                  :current-session-type session-type
                  :required-session-type required-session-type}))

      :moderator
      (when-not (contains? #{:moderator :admin :owner} session-type)
        (ex-info "Given session does not have the proper privileges to call this word function."
                 {:error-type ::not-privileged
                  :fname fname
                  :current-session-type session-type
                  :required-session-type required-session-type}))

      :admin
      (when-not (contains? #{:admin :owner} session-type)
        (ex-info "Given session does not have the proper privileges to call this word function."
                 {:error-type ::not-privileged
                  :fname fname
                  :current-session-type session-type
                  :required-session-type required-session-type}))

      :owner
      (when-not (contains? #{:owner} session-type)
        (ex-info "Given session does not have the proper privileges to call this word function."
                 {:error-type ::not-privileged
                  :fname fname
                  :current-session-type session-type
                  :required-session-type required-session-type}))

      (throw (ex-info "Unknown session-type in `check-privileges` function" {:session-type required-session-type})))))
