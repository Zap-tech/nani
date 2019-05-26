(ns test-utils.db
  "Testing utilities for nani when testing the database"
  (:require
   [mount.core :as mount]
   [clojure.test :refer [deftest is are testing]]

   [nani.server.config]
   [nani.server.db]))


(def test-default-config
  "Configuration to use when testing the database"
  "nani_test.fif")


(defn fixture-start []
  (binding [nani.server.config/*default-config-path* test-default-config]
    (-> (mount/only
         [#'nani.server.config/config
          #'nani.server.db/db])
        mount/start)))


(defn fixture-stop []
  (mount/stop))



(defmacro deftest-db
  [name & body]
  `(deftest ~name
     (fixture-start)
     ~@body
     (fixture-stop)))
    
