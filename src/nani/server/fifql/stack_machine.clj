(ns nani.server.fifql.stack-machine
  (:require
   [fifql.core :as fifql]

   [nani.server.fifql.lib.core :as lib.core :refer [import-nani-core-libs]]
   [nani.server.fifql.lib.user :as lib.user :refer [import-nani-user-libs]]
   [nani.server.fifql.lib.discussion :as lib.discussion :refer [import-nani-discussion-libs]]
   [nani.server.fifql.lib.post :as lib.post :refer [import-nani-post-libs]]
   [nani.server.fifql.lib.comment :as lib.comment :refer [import-nani-comment-libs]]
   [nani.server.fifql.lib.vote :as lib.vote :refer [import-nani-vote-libs]]
   [nani.server.fifql.lib.me :as lib.me :refer [import-nani-me-libs]]))


(def main-stack-machine
  (-> (fifql/create-stack-machine)

      ;; Libraries
      import-nani-core-libs
      import-nani-user-libs
      import-nani-discussion-libs
      import-nani-post-libs
      import-nani-comment-libs
      import-nani-vote-libs
      import-nani-me-libs))
