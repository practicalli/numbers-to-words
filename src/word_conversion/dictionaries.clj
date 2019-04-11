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

(def teens->word
  {"10" "ten"
   "11" "eleven"
   "12" "twelve"
   "13" "thirteen"
   "14" "fourteen"
   "15" "fifteen"
   "16" "sixteen"
   "17" "seventeen"
   "18" "eighteen"
   "19" "nineteen"})

(def tens->word
  {\2 "twenty"
   \3 "thirty"
   \4 "forty"
   \5 "fifty"
   \6 "sixty"
   \7 "seventy"
   \8 "eighty"
   \9 "ninety"})



;; previous approach using strings
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


;; zero to nine
#_(def single-digit
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

#_(def teens
    {"10" "ten"
     "11" "eleven"
     "12" "twelve"
     "13" "thirteen"
     "14" "fourteen"
     "15" "fifteen"
     "16" "sixteen"
     "17" "seventeen"
     "18" "eighteen"
     "19" "nineteen"})

;; tens
#_(def tens
    {"10" "ten"
     "20" "twenty"
     "30" "thirty"
     "40" "forty"
     "50" "fifty"
     "60" "sixty"
     "70" "seventy"
     "80" "eighty"
     "90" "ninety"})

;; hundreds (all the same, but a bit of an edge case)
#_(def hundreds
    {"100" "one hundred"
     "200" "two hundred"
     "300" "three hundred"
     "400" "four hundred"
     "500" "five hundred"
     "600" "six hundred"
     "700" "seven hundred"
     "800" "eight hundred"
     "900" "nine hundred"})




;; Generic Number levels
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; A dictionary of number levels
;; Indexed by the size of the number as a string
;; 100 has 3 characters, 1000000 has 7 characters, etc.

;; hundreds, thousands and millions all the same term regardless
;; of prefix number (one thousand, two thousand, etc.)
;; simply add number level as post-fix

#_(def number-levels
    "List of number levels."
    {3 "hundred" 4 "thousand" 6 "hundred thousand" 7 "million" 10 "billion" 13 "trillion"})


#_(def generic-number-levels
    {"x00"           "hundred"
     "x000"          "thousand"
     "x000000"       "million"
     "x000000000"    "billion"
     "x000000000000" "trillion"})


;; Basic error warnings
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

#_(def invalid-number
    {"-1" "Computer says no!"})


#_(def british-english-dictionary
    "Combination of dictionaries that include British English lookup
  values"
    (merge single-digit teens tens hundreds generic-number-levels))
