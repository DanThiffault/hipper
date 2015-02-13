(ns hipper.core
  (:require [clojure.zip :as zip]
            [clojure.string :refer [split replace]]))

(def make-node)

(def id-regex #"#[^.#]*")

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

(defn cleanup-hiccup-node [node]
  (->> node
       (filter #(not-nil-or-empty-coll? (second %)))
       (into {})))

(defn create-from-attributes [attrs] 
  (->> (concat [(keyword (:tag attrs)) 
        (cleanup-hiccup-node (dissoc attrs :tag :children))]
               (:children attrs))
       (filterv not-nil-or-empty-coll?)))

(defn make-node [node children] 
  (->> (extract-attributes node) 
       (merge {:children children})
       create-from-attributes))

(def branch? 
  (every-pred sequential? (comp not-empty (partial filter sequential?))))

(defn hiccup-zip [form]
  (zip/zipper branch? extract-children make-node 
              (make-node form (extract-children form))))
