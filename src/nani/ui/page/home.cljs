(ns nani.ui.page.home
  (:require
   [reagent.core :as r]
   [nani.ui.router :as router]
   [nani.ui.component.base-layout :refer [c-base-layout]]))


(defmethod router/page :nani/home
  [_]
  (fn []
    [c-base-layout {:container-opts {:class :home-main-container}}
     "home page"]))
