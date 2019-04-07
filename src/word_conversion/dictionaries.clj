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

;; zero to nine
(def single-digit
  {"0" "zero"
   "1" "one"
   "2" "two"
   "3" "three"
   "4" "four"
   "5" "five"
   "6" "six"
   "7" "seven"
   "8" "eight"
   "9" "nine"})

(def teens
  {"11" "eleven"
   "12" "twelve"
   "13" "thirteen"
   "14" "fourteen"
   "15" "fifteen"
   "16" "sixteen"
   "17" "seventeen"
   "18" "eighteen"
   "19" "nineteen"})

;; tens
(def tens
  {"10" "ten"
   "20" "twenty"
   "30" "thirty"
   "40" "forty"
   "50" "fifty"
   "60" "sixty"
   "70" "seventy"
   "80" "eighty"
   "90" "ninety"})


(def british-english-dictionary
  "Combination of dictionaries that include British English lookup
  values"
  (merge single-digit teens tens))


;; Generic Number levels
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; A sequential list of number levels
;; hundreds, thousands and millions - no difference in numbers,
;; simply add level as postfix

(def number-levels
  "List of number levels which are all the same regardless of any prefix
  number."
  ["hundred" "thousand" "million" "billion" "trillion"])
