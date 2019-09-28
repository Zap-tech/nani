(ns nani.ui.sub
  (:require
   [re-frame.core :as re]))


(re/reg-sub
 :router/active-page
 (fn [db] (:nani/active-page db)))
