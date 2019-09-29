(ns nani.ui.component.post-listing
  (:require
   [reagent.core :as r]
   [re-frame.core :as re]))


(defn c-post-element
  [{:keys [:post/id] :as post}]
  [:div.post-element
   {:key (str "post-element-" id)}
   [:span.post-title "Post Title"]
   [:span.post-submission "Submitted 2 Hours Ago by Bacon1989 to /d/general"]
   [:span.post-actions "View Comments (99)"]])


(defn c-post-listing
  [post-listing]
  [:div.post-listing {:key "post-listing"}
   (doall
    (for [post post-listing]
      ^{:key (str "post-" (:post/id post))}
      [c-post-element post]))])
