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


(defn clean-word-sequence
  "All number strings that are pronounced start with something other than zero.
  All zero numbers are removed, except where zero is the only value in the sequence.

  Arguments: Vector of positional numbers as strings
  Return: Vector of positional numbers as strings"

  [number-string]

  (if (= ["0"] number-string)
    ["0"]
    (filter #(not= \0 (first %)) number-string)))


(defn number-sequence
  "Convert a number into a sequence of numbers that also represent the
    number level

  Arguments: Integer or Long
  Return: Vector of strings"

  [number]

  {:pre [(<= 0 number)]} ; Exception if function called with negative number

  (loop [current-string      (str number)
         sequence-of-numbers []]
    (if (empty? current-string)
      (clean-word-sequence sequence-of-numbers)
      (recur (rest current-string)
             (conj sequence-of-numbers
                   (positional-number-string current-string))))))



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

;; We can just filter out all numbers that start with a zero,
;; as they would not be pronounced
;; would need to add a check for a single number in a sequence that is zero

#_(defn clean-word-sequence
    "All number strings should start with something other than zero"
    [number-string]
    (filter #(not= \0 (first %)) number-string))
;; => #'word-conversion.core/clean-word-sequence

#_(word-sequence 1024)
;; => ["1000" "000" "20" "4"]

#_(clean-word-sequence (word-sequence 1024))
;; => ("1000" "20" "4")


;; Add some defensive coding for a single zero sequence
#_(defn clean-word-sequence
    "All number strings should start with something other than zero"
    [number-string]
    (if (= ["0"] number-string)
      ["0"]
      (filter #(not= \0 (first %)) number-string)))
;; => #'word-conversion.core/clean-word-sequence

#_(clean-word-sequence ["0"])
;; => ["0"]


;; defining dictionaries
;;;;;;;;;;;;;

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

;; hundreds, thousands and millions - no difference in numbers,
;; simply add level as postfix

;; create a sequential list of number levels
#_(def number-levels
    "List of number levels."
    ["hundred" "thousand" "million" "billion" "trillion"])


;; Alternative idea:
;; seems like many more combinations will need to be defined within dictionaries.
#_(def number-levels
    {2 ["two" "twenty" "two hundred" "two thousand" "two hundred thousand" "two million"]})

#_(def number-word-dictionary
    (merge single-digit teens tens))


#_(map #(get number-word-dictionary %) (word-sequence 42))
;; => ("forty" "two")


#_(map #(get number-word-dictionary %) (word-sequence 2))
;; => ("two")


(defn number-level->word
  [number number-levels]
  (get number-levels number))
