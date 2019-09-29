(ns nani.ui.page.home
  (:require
   [reagent.core :as r]

   [nani.ui.router :as router]
   [nani.ui.component.base-layout :refer [c-base-layout]]
   [nani.ui.component.post-listing :refer [c-post-listing]]
   [nani.ui.component.post-sidebar :refer [c-post-sidebar]]))


(defmethod router/page :nani/home
  [_]
  (fn []
    [c-base-layout {:container-opts {:class :home-main-container}}
     [c-post-sidebar]
     [c-post-listing
      [{:post/id "test1"}
       {:post/id "test2"}]]]))
