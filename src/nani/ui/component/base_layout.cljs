(ns nani.ui.component.base-layout
  (:require
   [re-frame.core :as re]
   [reagent.core :as r]

   [nani.ui.component.navigation-header :refer [c-navigation-header]]))


(defn c-base-layout
  [{:keys [container-opts] :as opts} & children]
  (let [opts (dissoc opts :container-opts)]
    [:div.base-layout opts
     [:div.header [c-navigation-header]]
     [:div.main-margin
      [:div.main-container
       container-opts
       children]]
     [:div.footer
      [:div.copyright
       [:span "Copyright © 2019 Nani Minimal Social Platform"]
       [:div.spacer "|"]
       [:span "All rights reserved."]]]]))
