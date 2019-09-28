(ns nani.ui.page.discussion
  (:require
   [reagent.core :as r]
   [nani.ui.router :as router]))


(defmethod router/page :nani/discussion
  [{:keys [::router/name ::router/params ::router/query] :as page}]
  (fn []
    (.log js/console (clj->js page))
    [:div.discussion-listing
     (str "Discussion '" (:name params) "'")]))
