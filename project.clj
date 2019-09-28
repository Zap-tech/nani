(defproject nani "1.0.0"
  :description "A Quick and Minimal Social Platform"
  :url "http://github.com/benzap/nani"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.10.1"]
                 [org.clojure/clojurescript "1.10.520" :scope "provided"]
                 [org.clojure/core.async "0.4.500"]

                 [funcool/bide "1.6.0"]              ;; Routing Library (cljs)
                 [buddy "2.0.0"]                     ;; Security Library (clj)
                 [compojure "1.6.1"]                 ;; Routing Library (clj)
                 [com.taoensso/encore "2.115.0"]     ;; Utility Library (clj/cljs)
                 [environ "1.1.0"]                   ;; Configuration
                 [hiccup "1.0.5"]                    ;; HTML Generation
                 [http-kit "2.3.0"]                  ;; HTTP Server
                 [me.raynes/fs "1.4.6"]              ;; Filesystem Utilities
                 [mount "0.1.16"]                    ;; Dependency Management Library
                 [re-frame "0.10.8"]                 ;; CLJS React State Management for Reagent
                 [reagent "0.8.1"]                   ;; React Wrapper Library
                 [ring/ring-defaults "0.3.2"]        ;; Useful Http Middleware Defaults
                 [com.taoensso/timbre "4.10.0"]      ;; Logging Library

                 ;; fif libraries
                 [fif-lang/fif      "1.4.0"]         ;; Stack-based Scripting Language
                 [fif-lang/fifql    "1.4.0"]         ;; Stack-based Query API
                 [fif-lang/fifiql   "1.5.0-SNAPSHOT"];; Interactive Query Page for Fifql Development
                 [fif-lang/fifql-fx "1.4.0"]         ;; Reagent Fifql Effect Handler

                 ;; Database
                 [org.xerial/sqlite-jdbc "3.28.0"]   ;; Sqlite
                 [juxt/crux-core "19.09-1.4.0-alpha"]
                 [juxt/crux-jdbc "19.09-1.4.0-alpha"]
                 [juxt/crux-rocksdb "19.09-1.4.0-alpha"]]

  :source-paths ["src"]
  :uberjar-name "nani-server.jar"
  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]
  :repl-options {:init-ns nani.core}
  :figwheel {:css-dirs ["resources/public/css"]}
  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src" "dev"]
                :figwheel {:on-jsload "nani.core/rerender"}
                :compiler {:main cljs.user
                           :asset-path "/js/compiled/out"
                           :output-to "resources/public/js/compiled/nani.js"
                           :output-dir "resources/public/js/compiled/out"
                           :optimizations :none
                           :source-map-timestamp true}}

               {:id "prod"
                :source-paths ["src" "dev"]
                :compiler {:main nani.core
                           :asset-path "/js/compiled/out-prod"
                           :output-to "resources/public/js/compiled/nani.js.min"
                           :output-dir "resources/public/js/compiled/out-prod"
                           :optimizations :advanced
                           :source-map-timestamp true}}]}

  :profiles 
  {:dev 
   {:main nani.dev.user
    :source-paths ["src" "dev"]
    :dependencies [[cider/piggieback "0.4.1"]
                   [figwheel "0.5.19"]
                   [figwheel-sidecar "0.5.19"]]
    :plugins [[lein-cljsbuild "1.1.7"]
              [lein-figwheel "0.5.18"]
              [lein-npm "0.6.2"]
              [lein-ancient "0.6.15"]]
    :repl-options {:init-ns nani.dev.user
                   :nrepl-middleware [cider.piggieback/wrap-cljs-repl]
                   :port 9005}}})
