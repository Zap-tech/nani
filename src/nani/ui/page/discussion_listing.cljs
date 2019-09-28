(ns nani.ui.page.discussion-listing
  (:require
   [reagent.core :as r]
   [nani.ui.router :as router]))


(defmethod router/page :nani/discussion-listing
  [{:keys [::router/name ::router/params ::router/query] :as page}]
  (fn []
    [:div.discussion-listing
     "Discussion Listing"]))
