(ns hipper.core-test
  (:require [cljs.test]
            [clojure.zip :as zip]
            [hipper.core :as c])
  (:require-macros [cljs.test :refer [deftest testing is run-all-tests]]))

(deftest hiccup-zip-test
  (testing "Converts tag names to a tag attribute"
    (is (=  {:tag "div"}
           (zip/root (c/hiccup-zip [:div])))))
  (testing "it converts id to an attribute"
    (is (= {:tag "div" :id "foo"}
           (zip/root (c/hiccup-zip [:div#foo])))))
  (testing "it converts class names to an attribute"
    (is (= {:tag "div" :classes ["foo" "bar"]}
           (zip/root (c/hiccup-zip [:div.foo.bar]))))))

