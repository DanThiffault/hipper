(ns hipper.core
  (:require [clojure.zip :as zip]
            [clojure.string :refer [split]]))

; search hiccup-form [seq search steps] -> vector locations
; hiccup-zip
;  * convert child nodes to child zipper
;  * convert strings to text child (leaf)
;  * merge map of other properties

(defn leaf? [node] false)

(defn extract-children [node] (seq))

(defn extract-id [elem] 
  (let [id (re-find #"#[^.#]*" elem)]
    (if (nil? id) nil (subs id 1))))

(defn extract-classes [elem] 
  (let [groups (split elem #"\.")
        classes (rest groups)]
    classes))

(defn make-node [node children] 
  (let [elem (-> node first name)
        tag-name (re-find #"[^.#]*" elem)
        classes (extract-classes elem)
        attributes [:tag tag-name :classes classes :id (extract-id elem)]]
    (into {} (for [[k v] (partition 2 attributes) 
                   :when (not (or (nil? v) (empty? v)))] [k v]))))

(defn hiccup-zip [form]
  (->> (make-node form (extract-children form))
       (zip/zipper (comp not leaf?) extract-children make-node)))
