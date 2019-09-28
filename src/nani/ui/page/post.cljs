(ns nani.ui.page.post
  (:require
   [reagent.core :as r]
   [nani.ui.router :as router]))


(defmethod router/page :nani/post
  [{:keys [::router/name ::router/params ::router/query] :as page}]
  (fn []
    [:div.post
     (str "Discussion '" (:name params)"' - " (:post params))]))
