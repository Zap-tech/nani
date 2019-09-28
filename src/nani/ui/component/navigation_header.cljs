(ns nani.ui.component.navigation-header
  (:require
   [reagent.core :as r]))


(defn c-navigation-header
  []
  [:div.navigation-header
   [:div.logo
    [:span "Nani"]]
   [:div.side
    [:span "You are logged in as "]
    [:span.name "benzap"]
    [:div.sign-out
     [:span "("]
     [:div.button "sign out"]
     [:span ")"]]]])
