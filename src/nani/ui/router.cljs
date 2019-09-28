(ns nani.ui.router
  (:require
   [bide.core :as r]
   [re-frame.core :as re]
   [nani.ui.routes]
   [nani.ui.event :as ui.event]))   


(def router (r/router nani.ui.routes/main-routes))


(comment
 (r/match router "/?sort-by=test")
 (r/match router "/d/all?sort-by=top")
 (r/match router "/d/all/fkd87dkd98"))


(defmulti page ::name)


(defn on-navigate [name params query]
  (re/dispatch [::ui.event/set-active-page {::name name ::params params ::query query}]))


(r/start! router
          {:default :nani/home
           :on-navigate on-navigate})
