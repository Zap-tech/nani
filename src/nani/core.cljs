(ns nani.core
  (:require
   [taoensso.timbre :as log]
   [reagent.core :as r]
   [re-frame.core :as re]

   [nani.ui.component.base-layout :refer [c-base-layout]]
   [nani.ui.event :as ui.event]
   [nani.ui.router]
   [nani.ui.pages]))
   


(log/merge-config!
 {:min-level :warn})


(def production-config
  {:environment-name "Production"})


(defn c-app []
  [c-base-layout {}])


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
  (render))
