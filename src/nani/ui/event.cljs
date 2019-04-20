(ns nani.ui.event
  (:require
   [re-frame.core :as re]
   [fifql-fx.effects]))


(re/reg-event-fx
 ::init
 (fn [{:keys [db]}]))
