(ns hipper.core
  (:require [clojure.zip :as zip]
            [clojure.string :refer [split replace]]))

(def make-node)

(defn leaf? [node] 
  (or (string? node) (and (vector? node) (= 1 (count node)))))

(defn extract-children [node] 
  (filterv some?
           (mapv #(cond (vector? %1) (make-node %1 (extract-children %1))
                        (string? %1) %1 
                        :else nil) node)))

(defn extract-id [elem] 
  (let [id (re-find #"#[^.#]*" elem)]
    (if (nil? id) nil (subs id 1))))

(defn extract-classes [elem] 
  (-> elem (replace #"#[^.]*" "") (split #"\.") rest))

(defn extract-attributes [node]
  (let [elem (-> node first name)
        tag-name (re-find #"[^.#]*" elem)
        classes (extract-classes elem)
        id (extract-id elem)]
  (reduce merge {:tag tag-name :classes classes :id id} 
          (conj (filter map? node) {}))))

(def not-nil-or-empty-coll?
  (complement (some-fn nil? (every-pred coll? empty?))))

(defn make-node [node children] 
  (let [attributes (merge (extract-attributes node) {:children children})]
    (into {} (filter #(not-nil-or-empty-coll? (second %)) attributes))))

(defn hiccup-zip [form]
  (->> (make-node form (extract-children form))
       (zip/zipper (complement leaf?) :children make-node)))
