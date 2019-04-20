(ns nani.server.page.home
  (:require
   [hiccup.core :as hiccup]
   [hiccup.page :refer [include-js include-css]]))


(def dev-init-script "cljs.user.start();")
(def prod-init-script "nani.core.start();")


(defn main [req {:keys [dev-mode?] :or {dev-mode? false}}]
  (let [init-script (if dev-mode? dev-init-script prod-init-script)]
    (hiccup/html
     [:html
      [:head
       (include-css "/css/main.css")]
      [:body
       [:div#app
        [:div.loading "Loading..."]]
       (include-js "/js/compiled/nani.js")
       [:script {:type "text/javascript"} init-script]]])))

