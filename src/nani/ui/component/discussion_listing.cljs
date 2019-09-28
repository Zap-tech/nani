(ns nani.ui.component.discussion-listing
  (:require
   [reagent.core :as r]
   [re-frame.core :as re]))


(defn c-discussion-element
  [{:keys [] :as discussion}]
  [:div.discussion-element
   [:span.title "Discussion Title"]
   [:span.num-posts "# Posts"]
   [:span.subscriptions "# Subscriptions"]])



(defn c-discussion-listing
  [discussion-listing]
  [:div.discussion-listing
   (doall
    (for [discussion discussion-listing]
      []))])
      
  
