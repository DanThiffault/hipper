(ns hipper.search-test
  (:require [cljs.test]
            [clojure.zip :as zip]
            [hipper.core :refer [hiccup-zip]]
            [hipper.search :as s])
  (:require-macros [cljs.test :refer [deftest testing is run-all-tests]]))

(def test-zip (hiccup-zip [:div.container 
                           [:div.row 
                            [:div.col-md-4] 
                            [:div.col-md-4]
                            [:div.col-md-4]]]))

(deftest all-is-not-well
  (testing "the end is near"
    (is (= 0 1))))
