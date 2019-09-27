(ns nani.ui.component.base-layout
  (:require
   [re-frame.core :as re]
   [reagent.core :as r]))


(defn c-base-layout
  [{:keys [] :as opts} & children]
  [:div.base-layout
   [:div.header "header"]
   [:div.main-margin
    [:div.main-container
     children]]])
