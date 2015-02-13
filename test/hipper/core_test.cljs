(ns hipper.core-test
  (:require [cljs.test]
            [clojure.zip :as zip]
            [hipper.core :as c])
  (:require-macros [cljs.test :refer [deftest testing is run-all-tests]]))

(deftest hiccup-zip-test
  (testing "Converts tag names to a tag attribute"
    (is (=  [:div]
           (zip/root (c/hiccup-zip [:div])))))
  (testing "it converts id to an attribute"
    (is (= [:div {:id "foo"}]
           (zip/root (c/hiccup-zip [:div#foo])))))
  (testing "it converts class names to an attribute"
    (is (= [:div {:classes ["foo" "bar"] :id "blah"}]
           (zip/root (c/hiccup-zip [:div.foo.bar#blah])))))
  (testing "it merges default attribute map when it exists"
    (is (= [:div {:foo :bar :id "abc"}]
           (zip/root (c/hiccup-zip [:div#abc {:foo :bar}])))))
  (testing "it works recursively"
    (is (= [:div {:foo :bar :id "abc"} "My text"]
           (-> (c/hiccup-zip [:div [:div#abc {:foo :bar} "My text"]]) zip/down zip/node)))))

(deftest not-nil-or-empty-coll-test
  (testing "it returns true for nil items"
    (is (false? (c/not-nil-or-empty-coll? nil))))
  (testing "it returns false for a keyword"
    (is (true? (c/not-nil-or-empty-coll? :my-stuff))))
  (testing "it returns false for an empty collection"
    (is (false? (c/not-nil-or-empty-coll? '()))))
  (testing "it returns true for a collection with items"
    (is (true? (c/not-nil-or-empty-coll? (list :a :b :c))))))

