(ns word-conversion.core)

(def british-english-numbers
  {0 "zero"
   1 "one"})

(defn number->word
  "Converts a numeric whole number into a word representation
  Returns java.lang.string"

  [number dictionary]

  (get dictionary number))





;; REPL design journal
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Convert numbers to words using a hash-map dictionary
;; convert word using dictionary
#_(number->word 0 british-english-numbers)
