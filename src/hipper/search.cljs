(ns hipper.search
  (:require [hipper.core :refer [hiccup-zip extract-attributes cleanup-hiccup-node]]
            [clojure.zip :as zip]
            [clojure.data :as data]))

(defn filter-by [step] 
  (cond 
    (map? step) #(->> % zip/node extract-attributes (data/diff step) first nil?)
    (keyword? step) (-> [step] extract-attributes cleanup-hiccup-node filter-by)
    :else (constantly false)))

(defn expand-locs [locs] locs)


;; The axis functions path-part-> loc->[loc]
(defn rights [loc]
  (let [my-right (zip/right loc)]
    (if (nil? my-right) [loc] (cons loc (rights my-right)))))

(defn children [loc]
  (rights (zip/down loc)))

;; Axis funcitons
(defn axis [path expand]
  (fn [locs] 
    (filter (filter-by path) (expand locs))))

(defn self [path]
  (axis path identity))

(defn child [path] 
  (axis path (partial mapcat children)))

(defn depth-first-seq [loc]
  (let [n (zip/next loc)]
    (if (zip/end? n) (if (nil? loc) [] [loc]) (cons loc (depth-first-seq n)))))

(defn descendant [path] 
  (axis path (partial mapcat depth-first-seq)))

;; Main entry point
(defn search [start paths]
  (reduce 
    (fn [current cpath]
      ((fn [result path] 
         (if (fn? path) (path result) ((child path) result)))
       current 
       cpath)) start paths))
