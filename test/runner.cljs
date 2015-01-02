(ns test-runner
  (:require [hipper.core :as c])
  (:require-macros [cljs.test :refer [deftest testing is run-all-tests]]))

(enable-console-print!)

(defn runner [] 
  (if (cljs.test/successful? (run-all-tests #".*-test")) 0 1))
