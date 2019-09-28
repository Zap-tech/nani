(ns nani.ui.event
  (:require
   [re-frame.core :as re]
   [fifql-fx.effects]))


(re/reg-event-fx
 ::init
 (fn [{:keys [db]}]
   {:db
    {:nani/active-page :nani/home}}))


(re/reg-event-fx
 ::set-active-page
 (fn [{:keys [db]} [_ page]]
   {:db (assoc db :nani/active-page page)}))
