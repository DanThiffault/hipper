(ns hipper.search
  (:require [hipper.core :refer [hiccup-zip]]))

;
; path parts -> predicate functions
;   map -> assume child filter for attribute
;   fn -> assume already predicate
;   keyword -> assume child filter tag name with split from core
; fn -> leave be
(defn create-path-predicate [path])

(defn search-from [paths]
  ; map across paths converting :div into fn etc.
  ; comp all paths together
  );path fn 

