(ns hipper.core-test
  (:require [cljs.test]
            [hipper.core :as c])
  (:require-macros [cljs.test :refer [deftest testing is run-all-tests]]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 0 1))))
