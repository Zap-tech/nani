(ns nani.server.model.user-test
  (:require
   [clojure.test :refer [deftest is are testing]]
   [test-utils.db :refer [deftest-db]]
   [nani.server.model.user :as model.user]))


(deftest-db main-test
  (model.user/new-user!
   {:user-name "test_user"
    :full-name "Test User"
    :password-hash "here is a password"
    :email "john.doe@gmail.com"})
  
  (let [test-user (model.user/user-by-username "test_user")]
    (is (= (:user_name test-user) "test_user"))
    (is (= (:full_name test-user) "Test User"))))
