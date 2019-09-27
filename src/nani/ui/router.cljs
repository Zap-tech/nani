(ns nani.ui.router
  (:require
   [bide.core :as r]
   [nani.ui.routes]))


(defmulti page first)


