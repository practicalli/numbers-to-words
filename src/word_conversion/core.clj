(ns word-conversion.core
  (:require [word-conversion.dictionaries :refer [british-english-dictionary
                                                  number-levels]]))


(defn positional-number-string
  "Round a number down to its positional number level.

  Examples: 2345 becomes 2000"

  [number-string]

  (apply str
         (cons (first number-string)
               (map (constantly "0") (rest number-string)))))


(defn clean-number-sequence
  "All number strings that are pronounced start with something other than zero.
  All zero numbers are removed, except where zero is the only value in the sequence.

  Arguments: Vector of positional numbers as strings
  Return: Vector of positional numbers as strings"

  [number-string]

  (if (= ["0"] number-string)
    ["0"]
    (filter #(not= \0 (first %)) number-string)))


(defn rest-as-string
  [number]
  ^:helper
  (apply str (rest number)))


(defn parse-number
  "A rather brute force approach to parsing the sequence of numbers
  create a representative sequence of numbers that can be readily converted
  into words using a dictionary

  Arguments: string (representing a number)
  Return: A sequence of strings (representing )"

  ^:abstract-me-please

  [number-string]

  (let [size (count number-string)]
    (cond
      (= 1 size)  [number-string]
      (= 2 size)  (concat [(str (first number-string) "0")]
                          (parse-number (rest-as-string number-string)))
      (= 3 size)  (concat [(str (first number-string))"x00"]
                          (parse-number (rest-as-string number-string)))
      (= 4 size)  (concat [(str (first number-string))"x000" ]
                          (parse-number (rest-as-string number-string)))
      (= 5 size)  (concat [(apply str (take 2 number-string))  "x000"]
                          (parse-number (apply str (drop 2 number-string))))
      (= 6 size)  (concat [(str (first number-string)) "x00"]
                          (parse-number (rest-as-string number-string)))
      (= 7 size)  (concat [(str (first number-string)) "x000000"]
                          (parse-number (rest-as-string number-string)))
      (= 8 size)  (concat [(apply str (take 2 number-string)) "x000000"]
                          (parse-number (apply str (drop 2 number-string))))
      (= 9 size)  (concat [(str (first number-string)) "x00"]
                          (parse-number (rest-as-string number-string)))
      (= 10 size) (concat [(str (first number-string)) "x000000000"]
                          (parse-number (rest-as-string number-string))))))


(defn numbers->words
  "Convert a sequence of numbers to their word equivalents in a given
  dictionary

  Arguments: hash-map dictionary, vector of strings (representing numbers)"

  [dictionary number-sequence]

  (map dictionary number-sequence))



(defn andify-sentence
  "Correct the grammar of the words as a sentence, by placing `and` in
  the sentence.  `and` is inserted after every occurance of `hundred`
  if it is followed by another number

  Example: [one hundred one] becomes [one hundred and one]

  Arguments: vector of strings (representing a sequence of words)
  Return: vector of strings (as above but with grammar corrections)"

  [word-sequence]

  (loop [sequence     word-sequence
         and-sequence []]
    (if (empty? sequence)
      and-sequence
      (recur (rest sequence)
             (conj and-sequence
                   (first sequence)
                   (if (and (= (first sequence) "hundred")
                            (not (nil? (second sequence))))
                     "and"
                     nil))))))

(clojure.string/join
  " "
  (andify-sentence
    (numbers->words british-english-dictionary (parse-number "12345"))))



(defn speak-number-as-words
  [dictionary number]
  (clojure.string/join
    " "
    (andify-sentence
      (numbers->words dictionary (parse-number (str number))))))

#_(defn speak-number-as-words
    [dictionary number]
    (->> number
         str
         parse-number
         (numbers->words dictionary)
         andify-sentence
         (clojure.string/join " ")))


;; str makes nil disappear
(str "Hello" nil)
;; => "Hello"


;; Depreciated functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn digit->word
  "Converts a numeric whole number (digit) into a word representation
  Returns: java.lang.string"

  ^:depreciated

  [digit dictionary]

  (get dictionary digit))



(defn split-larger-numbers
  "Any number string larger than 99 should be split into its parts.

  For example: 200 should be [2 x00], 2000 should be [2 x000]

  Arguments: string (representing a number)
  Return: vector of one or more strings"

  ^:depreciated

  [number-string]

  (let [size (count number-string)]
    (cond
      (> 3 size)  [number-string]
      (= 3 size)  [(str (first number-string))"x00"]
      (= 4 size)  [(str (first number-string))"x000"]
      (= 5 size)  [(apply str (take 2 number-string))  "x000"]
      (= 6 size)  [(str (first number-string)) "x00" "x000"]
      (= 7 size)  [(str (first number-string)) "x000000"]
      (= 8 size)  [(apply str (take 2 number-string)) "x000000"]
      (= 9 size)  [(str (first number-string)) "x00 x000000"]
      (= 10 size) [(str (first number-string)) "x000000000"])))




(defn number-sequence
  "Convert a number into a sequence of numbers that also represent the
    number level

  Arguments: Integer or Long
  Return: Vector of strings"
  ^:depreciated

  [number]

  {:pre [(<= 0 number)]} ; Exception if function called with negative number

  (loop [current-string      (str number)
         sequence-of-numbers []]
    (if (empty? current-string)
      (clean-number-sequence sequence-of-numbers)
      (recur (rest current-string)
             (conj sequence-of-numbers
                   (positional-number-string current-string))))))




(defn multiple-number-levels
  [number]
  ^:depreciated
  (let [remainder (rem (count number) 3)
        levels    (quot (count number) 3)]
    (if (> 1 levels)
      (apply str (take remainder number) (multiple-number-levels (drop remainder)))
      number)))


(defn get-prefix-number
  "Gets the prefix of a number without the number level.

  Number levels go up in groups of three, so 1000 returns 1, 10000 returns 10.

  Arguments: String (representing a number with its positional number level)
  Return: String (representing just the prefix value)"

  [number-string]

  ^:depreciated

  (let [prefix (rem (count number-string) 3)
        levels (quot (count number-string) 3)]

    (cond
      (= prefix 0) (str (first number-string))
      (> 1 levels) (multiple-number-levels number-string)
      :else        (apply str (take prefix number-string)))))

(get-prefix-number "1000")
;; => "1"
(get-prefix-number "10000")
;; => "10"
(get-prefix-number "100000")
;; => "1"

;; if
;; 100 = 1
;; 1000 = 1
;; 10,000 = 10
;; 100,000 = 100
;; 1,000,000 = 1
(defn word-sequence
  "Convert numbers in a sequence to their corresponding words, using a dictionary

  Arguments: dictionary lookup table (hash-map), sequence of integer/long numbers
  Return: sequence of words (vector of strings)"

  ^:depreciated

  [dictionary number-sequence]

  (map (fn [positional-number]
         (if (<= (count positional-number) 3)
           (get dictionary positional-number)
           (str (get dictionary (get-prefix-number positional-number))
                " "
                (get number-levels (count positional-number)))))
       number-sequence))




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

#_(defn number-sequence
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

#_(number-sequence 23456)
;; => ["20000" "3000" "400" "50" "6"]

;; If called with a negative number, the function should return an error

#_(number-sequence -1)
;; java.lang.AssertionError
;; Assert failed: (<= 0 number)

;; Limitation
;; there is a bit of a gotcha in that if there is a zero value part way through a number,
;; then we would get 00 000 0000 etc.

;; We can just filter out all numbers that start with a zero,
;; as they would not be pronounced
;; would need to add a check for a single number in a sequence that is zero

#_(defn clean-number-sequence
    "All number strings should start with something other than zero"
    [number-string]
    (filter #(not= \0 (first %)) number-string))
;; => #'word-conversion.core/clean-number-sequence

#_(number-sequence 1024)
;; => ["1000" "000" "20" "4"]

#_(clean-number-sequence (number-sequence 1024))
;; => ("1000" "20" "4")


;; Add some defensive coding for a single zero sequence
#_(defn clean-number-sequence
    "All number strings should start with something other than zero"
    [number-string]
    (if (= ["0"] number-string)
      ["0"]
      (filter #(not= \0 (first %)) number-string)))
;; => #'word-conversion.core/clean-number-sequence

#_(clean-number-sequence ["0"])
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
    ["hundred" "thousand" "hundred thousand" "million" "billion" "trillion"])


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

;; numbers larger than 100
;;;;;;;;;;;;;;;;;;;;;;;;

;; If a number is larger than a hundred, we can just use the first digit
;; as a word and post-fix with the relevant number level (hundred,
;; thousand, etc.)

#_(word-sequence 101)
;; => ["100" "00" "1"]

#_(clean-word-sequence
    (word-sequence 101))
;; => ("100" "1")

;; If we count the number of digits in a stringified number,
;; then we get a consistent number level.

#_(count "200")
;; => 3

;; we can define a dictionary that is a lookup for number levels,
;; based on the string size

#_(def number-levels
    "List of number levels."
    {3 "hundred" 4 "thousand" 6 "hundred thousand" 7 "million" 10 "billion" 13 "trillion"})


(map (fn [positional-number]
       (if (<= (count positional-number) 2)
         (get british-english-dictionary positional-number)
         (str (get british-english-dictionary (str (first positional-number)))
              " "
              (get number-levels (count positional-number)))))
     ["100"])


(map (fn [positional-number]
       (if (<= (count positional-number) 2)
         (get british-english-dictionary positional-number)
         (str (get british-english-dictionary (str (first positional-number)))
              " "
              (get number-levels (count positional-number)))))
     ["100000"])
;; => ("one hundred thousand")


;; solution so far does not work for 10,000
;; so we need to add more logic to capture this case
(map (fn [positional-number]
       (if (<= (count positional-number) 2)
         (get british-english-dictionary positional-number)
         (str (get british-english-dictionary (str (first positional-number)))
              " "
              (get number-levels (count positional-number)))))
     ["10000"])
;; => ("one ")


(map (fn [positional-number]
       (if (<= (count positional-number) 2)
         (get british-english-dictionary positional-number)
         (str (get british-english-dictionary (str (first positional-number)))
              " "
              (get number-levels (count positional-number)))))
     ["10000"])


;; get the number level
;; 10000 => thousand

;; drop those numbers that represent the number level
;; 10000 => 10

(apply str
       (take (rem (count "10000") 3) "10000"))
;; => "10"

;; convert number after drop
;; 10 => "ten"

(defn get-prefix-number
  "Gets the prefix of a number without the number level.

  Number levels go up in groups of three, so "
  [number-string]
  (apply str
         (take (rem (count number-string) 3) number-string)))

(get-prefix-number "1")
;; => "1"
(get-prefix-number "10")
;; => "10"
(get-prefix-number "100")
;; => ""
(get-prefix-number "1000")
;; => "1"
(get-prefix-number "10000")
;; => "10"
(get-prefix-number "100000")
;; => ""
(get-prefix-number "1000000")
;; => "1"
(get-prefix-number "10000000")
;; => "10"
(get-prefix-number "100000000")
;; => ""

;; Partitioning
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(clojure.string/reverse "1234567")
;; => "7654321"

(partition-all
  3
  (clojure.string/reverse "1234567"))
;; => ((\7 \6 \5) (\4 \3 \2) (\1))

(defn partition-string-number
  [number-string]
  (partition-all
    3
    (clojure.string/reverse number-string)))

(partition-string-number "12001")
;; => ((\1 \0 \0) (\2 \1))


#_(partition-all 3 (word-sequence 1042))
;; => (("1000" "000" "40") ("2"))


;; seems like we need to reverse the sequence to partition correctly

#_(reverse
(word-sequence 1042))
;; => ("2" "40" "000" "1000")

#_(partition-all 3
(reverse
  (word-sequence 1042)))
;; => (("2" "40" "000") ("1000"))

#_(reverse
(partition-all 3
               (reverse
                 (word-sequence 1042))))
;; => (("1000") ("2" "40" "000"))

;; a double reverse seems ugly and its also reversing the elements
;; in each partition



#_(count (word-sequence 1042))
;; => 4

#_(mod
(count (word-sequence 1042))
3)
;; => 1


#_(quot
(count (word-sequence 1042))
3)
;; => 1

#_(rem
(count (word-sequence 1042))
3)
;; => 1



#_(word-sequence 56945781)
;; => ["50000000" "6000000" "900000" "40000" "5000" "700" "80" "1"]

#_(partition-all 3
(word-sequence 56945781))
;; => (("50000000" "6000000" "900000") ("40000" "5000" "700") ("80" "1"))


#_(quot
(count (word-sequence 56945781))
3)
;; => 2

#_(rem
(count (word-sequence 56945781))
3)
;; => 2




;; Abstractions - map-indexed
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; alternative approach for positional-number-string


(map-indexed (fn [index item] [index item]) "12345")
;; => ([0 \1] [1 \2] [2 \3] [3 \4] [4 \5])

(map-indexed (fn [index item] [(inc index) item]) "12345")
;; => ([1 \1] [2 \2] [3 \3] [4 \4] [5 \5])


(map-indexed (fn [index item] [item (inc index)]) "12345")
;; => ([\1 1] [\2 2] [\3 3] [\4 4] [\5 5])

;; now the position is associated, we can use that to pad numbers
;; if we count the size of the number string, we can subtract
;; the position from the count and add that many zeros to the end of the number


(map (fn [[item index]] [item (- 5 index)])
     '([\1 1] [\2 2] [\3 3] [\4 4] [\5 5]))
;; => ([\1 4] [\2 3] [\3 2] [\4 1] [\5 0])


;; expand on this example as the basis of defining a function
(let [number "12345"
      size   (count number)
      index  (map-indexed (fn [index item] [item (inc index)]) number)]
  (map
    (fn [[digit position]]
      [digit (- size position)])
    index))
;; => ([\1 4] [\2 3] [\3 2] [\4 1] [\5 0])


;; create the padding using repeat (join the stings after)
(take 4 (repeat "0"))
;; => ("0" "0" "0" "0")


(let [number "12345"
      size   (count number)
      index  (map-indexed (fn [index item] [item (inc index)]) number)]
  (map
    (fn [[digit position]]

      (apply str digit
             (repeat (- size position) "0")))
    index))


(defn positional-number-string-map-index
  [number-string]
  (let [size  (count number-string)
        index (map-indexed (fn [index item] [item (inc index)]) number-string)]
    (map
      (fn [[digit position]]

        (apply str digit
               (repeat (- size position) "0")))
      index)) )



;; cl-format
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

#_(require '[clojure.pprint :refer [cl-format]])

#_(defn int-word [n] (cl-format nil "~r" n))

#_(int-word 23010)
;; => "twenty-three thousand, ten"
