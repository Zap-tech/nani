(ns nani.ui.component.post-sidebar
  (:require
   [reagent.core :as r]
   [re-frame.core :as re]))


(defn c-post-sidebar
  []
  [:div.post-sidebar {:key "post-sidebar"}
   "post-sidebar"])
