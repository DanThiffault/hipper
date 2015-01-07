(defproject hipper "0.1.0-SNAPSHOT"
  :description "Zipper based search for hiccup forms"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2665"]]
  :plugins  [[lein-cljsbuild "1.0.4"]]
  :source-paths ["src"]
  :cljsbuild {:builds [{:id "dev"
                        :source-paths ["src" "test"]
                        :compiler {:output-to "target/cljs/hipper.js"
                                   :output-dir "target/cljs/hipper/"
                                   :optimizations :none
                                   :cache-analysis true
                                   :target :nodejs
                                   :source-map "target/cljs/hipper.js.map"}}]
              :test-commands {"test" ["nodejs" "node-test/runner.js" "target/cljs/hipper" "/..//hipper.js" "/hipper/core_test" "hipper/search_test"]}})
