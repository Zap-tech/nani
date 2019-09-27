(ns nani.ui.page.home
  (:require
   [reagent.core :as r]
   [nani.ui.router :as router]))


(defmethod router/page :default
  [_]
  (fn []
   [:div.home-page
    "home page"]))
