;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Dictionaries for Number to Word Conversion
;;
;; Dictionaries used as lookup tables to convert numbers into their
;; respective word representations
;;
;; Author(s): John Stevenson
;; Date created: 7th April 2019
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(ns word-conversion.dictionaries)


;; Specific number lookups
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def digit->word
  "Lookup for single digit words and number levels hundred, thousand, etc.
  Not used for numbers between 10 and 99."
  {\0 "zero"
   \1 "one"
   \2 "two"
   \3 "three"
   \4 "four"
   \5 "five"
   \6 "six"
   \7 "seven"
   \8 "eight"
   \9 "nine"})


