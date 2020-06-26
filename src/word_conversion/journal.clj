(ns word-conversion.journal
  (:require [word-conversion.core :refer :all]
            [word-conversion.dictionaries :refer :all]))



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

#_(defn cclean-number-sequencelean-number-sequence
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
  ^:;; Refactored algorithm
  ;; take a number
  ;; convert to string
  ;; split into individual digits that represent the number positional ("21" => "20 1")
  ;; partition from right into groups of threes
  ;; process each partition of three (hundreds, tens, ones)
  ;; apply relevant rules - andify, hyphenate, etc
  ;; process whole sequence (thousands, millions, billions)
  ;; turn in to a string sentence

depreciated
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



;; second approach - far too low level
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;




(defn convert-cheque
  "Convert cheque and speak as words functions should be top level
  functions (API) for this namespace."

  [amount]

  ;; convert to string
  ;; split to pounds and pence on .

  (let [amout-string   (str amount)
        [pounds pence] (clojure.string/split amout-string #"\.")]

    (str  ((speak-number-as-words british-english-dictionary pounds))
          "pounds and "
          (speak-number-as-words british-english-dictionary pence)
          "pence")))

;; the last digit and second last digit not=0 then add hyphen



(defn positional-number-string
  "Round a number down to its positional number level.

  Examples: 2345 becomes 2000

  Arguments: String representing a whole number
  Return: Vector of strings (representing each digit and its number
  level, eg. hundred, thousand, etc.)"

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
  "Simple helper function to convert a sequence of characters to a
  string.

  Arguments: A sequence of characters that represent the digits of a number
  Return: A string of those characters"

  [number-as-characters]

  (apply str (rest number-as-characters)))





(defn andify-sentence
  "Correct the grammar of the words as a sentence, by placing `and` in
  the sentence.  `and` is inserted after every occurance of `hundred`
  if it is followed by another number

  Example: [one hundred one] becomes [one hundred and one]

  TODO: refactor to create a generalised function that will update a
  number sequence (we should apply this to a grouped set of number
  rather than the whole sequence of word numbers).

  Arguments: vector of strings (representing a sequence of words)
  Return: vector of strings (as above but with grammar corrections)"

  ^:deprecated

  [word-sequence]

  (loop [sequence     word-sequence
         and-sequence []]
    (if (empty? sequence)
      and-sequence
      (recur (rest sequence)
             (if (and (= (first sequence) "hundred")
                      (not (nil? (second sequence))))
               "and"
               nil)
             (if (not= (first sequence)))
             (conj and-sequence
                   (first sequence)
                   )))))



(defn parse-number
  "A rather brute force approach to parsing the sequence of numbers
  create a representative sequence of numbers that can be readily converted
  into words using a dictionary

  TODO: should first partition the numbers in to groups of threes,
  starting from the right hand side.  A common pattern can then be applied
  overall by groups of threes.  Also specific rules (andify, hyphenate) can be
  applied within each group.

  Arguments: string (representing a number)
  Return: A vector of strings (representing )"

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

  Arguments: hash-map dictionary, vector of strings (representing numbers)
  Return: Sequence of strings (each string is a word representing a number)"

  ^:refactor

  [dictionary number-sequence]

  (map dictionary number-sequence))


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


;; Tests for this approach (and previous approach, a bit of a mess really).


(deftest number->british-english-test
  "Test the conversion of numerical whole numbers to
   strings containing British English words as a sentence"

  #_(testing "Sample data tests - single values"
      (is (= "zero" (sut/digit->word 0 sut/british-english-numbers)))
      (is (= "one"  (sut/digit->word 1 sut/british-english-numbers))))

  #_(testing "Sample data tests - double figure values"
      (is (= "20" (sut/positional-number-string "21"))))

  #_(testing "Generating sequences of numbers with positional number level"
      (is (= ["20" "1"]   (sut/number-sequence 21)))
      (is (= ["2000"]     (sut/number-sequence 2000)))
      (is (= ["2000" "1"] (sut/number-sequence 2001))))

  ;; 2001 probably should be "2" "x000" "1"


  #_(testing "Convert sequences of numbers to words"
      (is (= ["zero"]
             (sut/word-sequence british-english-dictionary ["0"])))
      (is (= ["one"]
             (sut/word-sequence british-english-dictionary ["1"])))
      (is (= ["twenty"]
             (sut/word-sequence british-english-dictionary ["20"])))
      (is (= ["twenty" "one"]
             (sut/word-sequence british-english-dictionary ["20" "1"])))
      (is (= ["ninety" "nine"]
             (sut/word-sequence british-english-dictionary ["90" "9"])))
      (is (= ["one hundred"]
             (sut/word-sequence british-english-dictionary ["100"])))
      (is (= ["one hundred" "one"]
             (sut/word-sequence british-english-dictionary ["100" "1"])))
      (is (= ["nine hundred" "ninety" "nine"]
             (sut/word-sequence british-english-dictionary ["900" "90" "9"])))
      (is (= ["one thousand"]
             (sut/word-sequence british-english-dictionary ["1000"])))
      (is (= ["one thousand" "one"]
             (sut/word-sequence british-english-dictionary ["1000" "1"])))
      )

  #_(testing "Parsing a number into a sequence of numbers"
      (is (= ["0"],
             (sut/parse-number "0")))
      (is (= ["20" "1"],
             (sut/parse-number "21")))
      (is (= ["1" "x00"],
             (sut/parse-number "100")))
      (is (= ["10" "x000" "5" "x00" "40" "5"]
             (sut/parse-number "10545"))))

  (testing "Speaking the words as numbers"
    (is (= "zero"
           (sut/speak-number-as-words british-english-dictionary 0)))
    (is (= "one"
           (sut/speak-number-as-words british-english-dictionary 1)))
    (is (= "twenty one"
           (sut/speak-number-as-words british-english-dictionary 21)))
    (is (= "one hundred and five"
           (sut/speak-number-as-words british-english-dictionary 105)))
    (is (= "one hundred and twenty three"
           (sut/speak-number-as-words british-english-dictionary 123)))
    (is (= "one thousand and five"
           (sut/speak-number-as-words british-english-dictionary 1005)))
    (is (= "one thousand and forty two"
           (sut/speak-number-as-words british-english-dictionary 1042)))
    (is (= "one thousand one hundred and five"
           (sut/speak-number-as-words british-english-dictionary 1105)))
    (is (= "fifty six million nine hundred and fourty five thousand seven hundred and eighty one"
           (sut/speak-number-as-words british-english-dictionary 56945781)))
    (is (= "nine hundred and ninety nine million nine hundred and ninety nine thousand
             nine hundred and ninety nine"
           (sut/speak-number-as-words british-english-dictionary 999999999))))
  )




;; partition approach - revisited
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; I missed something when I first looked at partition and had
;; identified all the right pieces, except I did not bring them
;; all together.



;; partition will drop any digits that do not make up the group size.
;; so we should use partition all on any group that is smaller.
;; Or add a check that the number size is divisible exactly by group size.
(partition 3 "0")
;; => ()

(partition-all 3 "0")
;; => ((\0))

(partition-all 3 "321")
;; => ((\3 \2 \1))

(partition 3 "7654321")

(reduce str
        (partition 3 "321"))


;; Where the number is not in a group of three,
;; we can use rem and quot to see how it should be grouped.

;; if we take the rem number of digits from the start of the number
;; then we can partition the rest correctly.



(partition-all 3 (take 1 "4321"))
;; => ((\4))

(drop 1 "4321")
;; => (\3 \2 \1)

(partition 3 (drop 1 "4321"))
;; => ((\3 \2 \1))

(conj
  (partition-all 3 (take 1 "4321"))
  (partition 3 (drop 1 "4321"))
  )
;; => (((\3 \2 \1)) (\4))


(concat
  (partition-all 3 (take 1 "4321"))
  (partition 3 (drop 1 "4321"))
  )
;; => ((\4) (\3 \2 \1))



(partition-number-string "987654321")
;; => ((\9 \8 \7) (\6 \5 \4) (\3 \2 \1))




;; converting numbers to a sequence of words
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(map #(character->number-word dictionary/digit->word %)
     (partition-number-string "1"))
;; => (nil)

;; its a sequence of sequences, silly...

(for [group (partition-number-string "1")]
  (map #(character->number-word dictionary/digit->word %)
       group))
;; => (("one"))


(for [group (partition-number-string "10")]
  (map #(character->number-word dictionary/digit->word %)
       group))
;; => (("one" "zero"))


(for [group (partition-number-string "100")]
  (map #(character->number-word dictionary/digit->word %)
       group))
;; => (("one" "zero" "zero"))

;; leave "zero" in for now so we can easily maintain the position
;; strip out "zero" when all grammar rules have been applied
;; and all is left is convert to a string.


(for [group (partition-number-string "1000")]
  (map #(character->number-word dictionary/digit->word %)
       group))
;; => (("one") ("zero" "zero" "zero"))

(for [group (partition-number-string "10000")]
  (map #(character->number-word dictionary/digit->word %)
       group))
;; => (("one" "zero") ("zero" "zero" "zero"))

(for [group (partition-number-string "9876543210")]
  (map #(character->number-word dictionary/digit->word %)
       group))
;; => (("nine") ("eight" "seven" "six") ("five" "four" "three") ("two" "one" "zero"))



;; partitioning with partition all for the section of the number not divisible by three does work, however, we are left with one partition smaller than the rest.

;; having uneven partitions when trying to convert to words with specific rules leads to complexity. If the partitions can be padded at the front, then things are simpler.

;; could pad using the `partition`, but it puts the padding to the right,
;; which breaks the number value
(partition 3 3 [\0] "15")
;; => ((\1 \5 \0))

;; we can concatonate zero characters on to the front of a character
(concat (repeat 2 \0) '(\1) )
;; => (\0 \0 \1)

;; we can do it manually by generating the right number of zeros based on the remainer
;; and group size
(conj (repeat (- group-size remainder) \0))


;; putting it all together
#_(concat
  (concat (repeat (- group-size remainder) \0)
          (partition-all group-size (take remainder number-string)))
  (partition group-size (drop remainder number-string)))


;; NPE
#_(character-sequence->word-sequence
    dictionary/digit->word
    (partition-number-string "321"))




;; parse-number
;; convert char sequence - word sequence
;; -- map function that calls char to number based on grammar rules

#_(for [group (partition-number-string "9876543210")]
    (map #(character->number-word dictionary/digit->word %)
         group))
;; => (("nine") ("eight" "seven" "six") ("five" "four" "three") ("two" "one" "zero"))




;; (reduce str '(\1 \3))


;; rather than use q pre-condition, which would have to be long and prevent us from using count, defensive programming around the initial input


;; {:pre [(<= 0 (Long. number-string))]}
