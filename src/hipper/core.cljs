(ns hipper.core
  (:require [clojure.zip :as zip]
            [clojure.string :refer [split]]))

; search hiccup-form [seq search steps] -> vector locations

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
  (let [groups (split elem #"\.")
        classes (rest groups)]
    classes))

(defn extract-attributes [node]
  (reduce merge {} (conj (filter map? node) {})))

(defn make-node [node children] 
  (let [elem (-> node first name)
        tag-name (re-find #"[^.#]*" elem)
        classes (extract-classes elem)
        id (extract-id elem)
        attributes {:tag tag-name :classes classes :id id :children children}
        attributes (merge (extract-attributes node) attributes)]
    (into {} (filter (fn [[_ v]] (not (or (nil? v) (and (coll? v) (empty? v))))) attributes))))

(defn hiccup-zip [form]
  (->> (make-node form (extract-children form))
       (zip/zipper (comp not leaf?) :children make-node)))
