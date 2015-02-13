(ns hipper.search-test
  (:require [cljs.test]
            [clojure.zip :as zip]
            [clojure.data :as data]
            [hipper.core :refer [hiccup-zip]]
            [hipper.search :as s])
  (:require-macros [cljs.test :refer [deftest testing is run-all-tests]]))

(def test-zip (hiccup-zip [:div.container#main 
                           [:div.row 
                            [:div.col-md-4] 
                            [:div.col-md-4]
                            [:div.col-md-4]]]))

(defn no-diff? [a b]
  (let [result (data/diff a b)]
    (and (nil? (first result)) (nil? (second result)))))

(deftest filter-by-test
  (testing "A map must match all passed properties"
    (is (false? ((s/filter-by {:id "junk"}) test-zip))))
  (testing "A map must allow other properties not specified"
    (is (true? ((s/filter-by {:id "main"}) test-zip))))
  (testing "Keywords are turned into hiccup maps"
    (is (true? ((s/filter-by :div) test-zip)))
    (is (false? ((s/filter-by :div.container#foo) test-zip)))
    (is (true? ((s/filter-by :div.container#main) test-zip)))))

(deftest search-test
  (testing "Searches starting with the given loc"
    (is (= [test-zip] (s/search [test-zip] [(s/self :div.container#main)])))
    (is (= [] (s/search [test-zip] [(s/self :body)]))))
  (testing "Child axis work"
    (is (= [(-> test-zip zip/down)] (s/search [test-zip] [(s/child :div)]))))
  (testing "Searches default to the child axis"
    (is (= [(-> test-zip zip/down)] (s/search [test-zip] [:div.row]))))
  (testing "Search recusively check subsequent paths"
    (is (no-diff? (-> test-zip zip/down s/children) 
                  (s/search [test-zip] [:div :div.col-md-4]))))
  (testing "A failed search returns an empty vector"
    (is (no-diff? [] 
                  (s/search [test-zip] [:div :div#nothere]))))
  (testing "Descendents match all descendents"
    (is (no-diff? (-> test-zip zip/down s/children) 
                  (s/search [test-zip] [(s/descendant :div.col-md-4)])))))
