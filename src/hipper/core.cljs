(ns hipper.core
  (:require [clojure.zip :as zip]
            [clojure.string :refer [split replace]]))

(def make-node)

(def id-regex #"#[^.#]*")

(def leaf? (some-fn string? 
                    (every-pred vector? #(= 1 (count %)))))

(defn extract-children [node] 
  (->> node
      (mapv #(cond (vector? %) (make-node % (extract-children %))
                   (string? %) % 
                   :else nil))
       (filterv some?)))

(defn extract-id [elem] 
  (some-> (re-find id-regex elem) (subs 1)))

(defn extract-classes [elem] 
  (-> elem (replace id-regex "") (split #"\.") rest))

(defn extract-attributes [node]
  (let [elem (-> node first name)
        tag-name (re-find #"[^.#]*" elem)
        classes (extract-classes elem)
        id (extract-id elem)]
  (reduce merge {:tag tag-name :classes classes :id id} 
          (conj (filter map? node)))))

(def not-nil-or-empty-coll?
  (complement (some-fn nil? (every-pred coll? empty?))))

(defn make-node [node children] 
  (->> (extract-attributes node) 
       (merge {:children children})
       (filter #(not-nil-or-empty-coll? (second %)))
       (into {})))

(defn hiccup-zip [form]
  (zip/zipper (complement leaf?) :children make-node 
              (make-node form (extract-children form))))
