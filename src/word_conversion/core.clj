(ns word-conversion.core)

(def british-english-numbers
  {0 "zero"
   1 "one"})

(defn number->word
  "Converts a numeric whole number into a word representation
  Returns java.lang.string"

  [number dictionary]

  (get dictionary number))





;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; REPL design journal
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Convert numbers to words using a hash-map dictionary
;; convert word using dictionary
#_(number->word 0 british-english-numbers)





;; Splitting up a number into individual parts
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; We cant partition a number
(partition 1 222)

;; We cant count a number
#_(count 222)

;; If we cant partition a number, lets change it to a string.  A string is a collection of chars after all.

;; so we can partition a string

(partition 1 "1234")
;; => ((\1) (\2) (\3) (\4))



(map Integer.
     (partition 1 "1234"))

(map #(Integer. %)
     (partition 1 "1234"))


(Integer. "42")
;; => 42

;; integer doesnt work with a character
(Integer. \1)


(map str
     (partition 1 "1234"))
;; => ("clojure.lang.LazySeq@50" "clojure.lang.LazySeq@51" "clojure.lang.LazySeq@52" "clojure.lang.LazySeq@53")

(apply str
       (partition 1 "1234"));; => "clojure.lang.LazySeq@50clojure.lang.LazySeq@51clojure.lang.LazySeq@52clojure.lang.LazySeq@53"

(defn partition-number
  [numbers]
  (for [number numbers]
    [(Integer. (str number))]))

(partition-number "1234")
;; => ([1] [2] [3] [4])




;; different approaches to dictionaries
;;;;;;;;;;;;;

;; zero to nine

;; tens

;; hundreds

;; thousands

;; millions


;; create a sequential list of number levels

;; use the number levels for groups of three numbers
(def number-levels
  "List of number levels.  "
  ["hundred" "thousand" "million" "billion" "trillion"])

(def number-levels
  {2 ["two" "twenty" "two hundred" "two thousand" "two hundred thousand" "two million"]})

(defn number-level->word
  [number number-levels]
  (get number-levels number))
