(ns hipper.search-test
  (:require [cljs.test]
            [clojure.zip :as zip]
            [hipper.core :refer [hiccup-zip]]
            [hipper.search :as s])
  (:require-macros [cljs.test :refer [deftest testing is run-all-tests]]))

(def test-zip (hiccup-zip [:div.container#main 
                           [:div.row 
                            [:div.col-md-4] 
                            [:div.col-md-4]
                            [:div.col-md-4]]]))

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
  (testing "Searches default to the self axis"
    (is (= [test-zip] (s/search [test-zip] [:div.container#main])))))
