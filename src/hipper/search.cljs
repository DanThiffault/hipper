(ns hipper.search
  (:require [hipper.core :refer [hiccup-zip extract-attributes cleanup-hiccup-node]]
            [clojure.zip :as zip]
            [clojure.data :as data]
            [cljs.core.match :refer-macros [match]]))

(def self)
(def child)

(defn filter-by [step] 
  (cond 
    (map? step) #(->> % zip/node (data/diff step) first nil?)
    (keyword? step) (-> [step] extract-attributes cleanup-hiccup-node filter-by)
    :else (constantly false)))

(defn expand-locs [locs] locs)

(defn search [current paths]
  ((fn [result path] 
     (if (fn? path) (path result) ((self path) result)))
   current 
   (first paths)))


;; The axis functions path-part-> loc->[loc]
(defn self [path]
  (fn [locs] (filter (filter-by path) locs)))

(defn child [path] )
