(ns word-conversion.core)

(def british-english-numbers
  {0 "zero"
   1 "one"})

(defn digit->word
  "Converts a numeric whole number (digit) into a word representation
  Returns: java.lang.string"

  [digit dictionary]

  (get dictionary digit))


(defn positional-number-string
  "Round a number down to its positional number level.

  Examples: 2345 becomes 2000"
  [number-string]
  (apply str
         (cons (first number-string)
               (map (constantly "0") (rest number-string)))))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; REPL design journal
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Convert numbers to words using a hash-map dictionary
;; convert word using dictionary
#_(digit->word 0 british-english-numbers)


;; Splitting up a number into individual parts
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; We cant partition a number so lets change it to a string.  A string is a collection of chars after all.

;; convert to a string
#_(str 24)
;; => "24"

#_(partition 1 "1234")
;; => ((\1) (\2) (\3) (\4))


;; Using for just splits to individual words
#_(defn partition-number
    [numbers]
    (for [number numbers]
      [(Integer. (str number))]))

#_(partition-number "1234")
;; => ([1] [2] [3] [4])


;; create a sequence of strings representing numbers
#_(clojure.string/split "24" #"")
;; => ["2" "4"]

;; We loose some of the sense of number level though.
;; Is 2 the value two or is it twenty?
;; We would need to track where we are in the sequence, relative to the end

;; recursive function to create a sequence of positional numbers
;;;;;;;;;;;;;;;;;;;;;;;;
;; get the first value
;; map (constantly 0) over the rest
;; conjoin
;; recur...

#_(defn positional-numbers-recursive
    [numbers]
    (let [string-numbers (str numbers)]
      (conj
        (map (constantly 0) (rest string-numbers))
        (first string-numbers))))

#_(positional-numbers-recursive 21)
;; => (\2 0)

;; still not quite the right format

#_(conj (first "2345") (map (constantly "0") (rest "2345")))


#_(map (constantly "0") (rest "2345"))
;; => ("0" "0" "0")



#_(mapcat (constantly "0") (rest "2345"))
;; => (\0 \0 \0)


#_(merge [] (first "2345") (map (constantly "0") (rest "2345")))
;; => [\2 ("0" "0" "0")]


#_(cons (first "2345") (map (constantly "0") (rest "2345")))
;; => (\2 "0" "0" "0")

#_(apply str
         (cons (first "2345") (map (constantly "0") (rest "2345"))))
;; => "2000"





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
