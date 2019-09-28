(ns nani.ui.routes
  (:require
   [bide.core :as r]))


(def main-routes
  [["/" :nani/home]
   ["/d/:name" :nani/discussion]
   ["/discussions" :nani/discussion-listing]
   ["/d/:name/:post" :nani/post]])
