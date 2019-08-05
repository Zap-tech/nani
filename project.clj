(defproject nani "1.0.0"
  :description "A Quick and Minimal Social Platform"
  :url "http://github.com/benzap/nani"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}

  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.520" :scope "provided"]
                 [org.clojure/core.async "0.4.490"]

                 [bidi "2.1.5"]                      ;; Routing Library (cljs)
                 [compojure "1.6.1"]                 ;; Routing Library (clj)
                 [com.taoensso/encore "2.109.0"]     ;; Utility Library (clj/cljs)
                 [environ "1.1.0"]                   ;; Configuration
                 [hiccup "1.0.5"]                    ;; HTML Generation
                 [http-kit "2.3.0"]                  ;; HTTP Server
                 [me.raynes/fs "1.4.6"]              ;; Filesystem Utilities
                 [mount "0.1.16"]                    ;; Dependency Management Library
                 [re-frame "0.10.6"]                 ;; CLJS React State Management for Reagent
                 [reagent "0.8.1"]                   ;; React Wrapper Library
                 [ring/ring-defaults "0.3.2"]        ;; Useful Http Middleware Defaults
                 [com.taoensso/timbre "4.10.0"]      ;; Logging Library

                 ;; fif libraries
                 [fif-lang/fif      "1.4.0"]         ;; Stack-based Scripting Language
                 [fif-lang/fifql    "1.4.0"]         ;; Stack-based Query API
                 [fif-lang/fifiql   "1.5.0-SNAPSHOT"];; Interactive Query Page for Fifql Development
                 [fif-lang/fifql-fx "1.4.0"]         ;; Reagent Fifql Effect Handler

                 ;; Database
                 [org.clojure/java.jdbc "0.7.9"]     ;; Database Handler
                 [org.xerial/sqlite-jdbc "3.23.1"]   ;; Sqlite
                 [honeysql "0.9.4"]]                 ;; SQL Abstraction Library

  :source-paths ["src"]
  :uberjar-name "nani-server.jar"
  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]
  :repl-options {:init-ns nani.core}

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
    :dependencies []
    :plugins [[lein-cljsbuild "1.1.7"]
              [lein-figwheel "0.5.18"]
              [lein-npm "0.6.2"]
              [lein-ancient "0.6.15"]]
    :repl-options {:init-ns nani.dev.user
                   :port 9005}}})
