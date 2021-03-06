(ns nani.ui.page.error
  (:require
   [reagent.core :as r]
   [nani.ui.router :as router]))


(defmethod router/page :default
  [{:keys [::router/name ::router/params ::router/query] :as page}]
  (fn []
    (.log js/console (clj->js page))
    [:div.error-page
     "We've experienced an error!"]))
