(ns nani.ui.page.error
  (:require
   [reagent.core :as r]
   [nani.ui.router :as router]))


(defmethod router/page :error-page
  [_]
  (fn []
   [:div.error-page
    "We've experienced an error"]))
