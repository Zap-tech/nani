(ns nani.server.auth.core
  (:require
   [buddy.hashers :as hashers]
   [nani.server.config :refer [config]]))


(defn- salt [password]
  (let [secret (-> config :database :secret)]  
    (str "nani-" password "-" secret)))


(defn encrypt
    [password]
    (hashers/derive (salt password)))


(defn check
  [password password-hash]
  (hashers/check (salt password) password-hash))



;;(check "test" (encrypt "test"))
