(ns nani.core
  (:require
   [taoensso.timbre :as log]
   [reagent.core :as r]
   [re-frame.core :as re]

   [nani.ui.component.base-layout :refer [c-base-layout]]
   [nani.ui.event :as ui.event]
   [nani.ui.sub :as ui.sub]
   [nani.ui.router]
   [nani.ui.pages]))
   


(log/merge-config!
 {:min-level :warn})


(def production-config
  {:environment-name "Production"})


(defn c-app []
  (let [active-page (re/subscribe [:router/active-page])]
    [nani.ui.router/page @active-page]))


(defn ^:export render []
  (r/render c-app (.querySelector js/document "#app")))


(defn ^:export start
  ([{:keys [name]}]
   (log/info (str "Initializing Nani [" name "]"))
   (render)
   (re/dispatch-sync [::ui.event/init]))
  ([] (start production-config)))


(defn rerender []
  (log/info "Rerendering...")
  (render)
  (r/force-update-all))
