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


(defn number-sequence
  "Convert a number into a sequence of numbers that also represent their
  number level"
  [number]

  []
  )


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

#_(defn rounded-number-string
    "Round a number down to its number level.

  Examples: 2345 becomes 2000"
    [number-string]
    (apply str
           (cons (first number-string)
                 (map (constantly "0") (rest number-string)))))

#_(defn word-sequence
    "Convert a number into a sequence of numbers that also represent the
  number level"
    [number]
    {:pre [(<= 0 number)]}
    (loop [current-string      (str number)
           sequence-of-numbers []]
      (if (empty? current-string)
        sequence-of-numbers
        (recur (rest current-string)
               (conj sequence-of-numbers
                     (rounded-number-string current-string))))))

#_(word-sequence 23456)
;; => ["20000" "3000" "400" "50" "6"]

;; If called with a negative number, the function should return an error

#_(word-sequence -1)
;; java.lang.AssertionError
;; Assert failed: (<= 0 number)

;; Limitation
;; there is a bit of a gotcha in that if there is a zero value part way through a number,
;; then we would get 00 000 0000 etc.
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
