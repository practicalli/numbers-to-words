(ns word-conversion.core
  (:require [word-conversion.dictionaries :as dictionary]))



(defn- hyphenated-pound-words
  "Convert two word numbers to a hyphenated word.

  If the last two numbers of each three groups of numbers in pounds
  are not zero, then hyphenate those two words.

  Example: 79.00 => seventy-nine

  Arguments: Vector of strings (representing numbers grouped in threes)
  Return: Vector of strings"

  ^:grammar-rule

  [number-triple]

  number-triple)


(defn- andify-word-group
  " "
  ^:grammar-rule
  []
  )



(defn partition-number-string
  "Partition a string representing a whole number into groups
  (group-size of 3 to represent how we speak numbers).  Grouping
  is from the right hand side.

  If the string number is divisible by group size without remainder, then we
  can just partition.

  Otherwise calculate the remainder of dividing the number by group-size,
  take the remainder number of elements from the head of the number and
  use partion-all, and partition the rest.  Then combine the results of the two.

  Precondition: number must be 0 or greater
  Arguments: String representing a number
  Return: Sequence of one or more sequences of characters"

  [number-string]

  (let [group-size  3
        number-size (count number-string)
        quotient    (quot number-size group-size)
        remainder   (rem number-size group-size)]

    (if (= 0 remainder)
      (partition group-size number-string)

      ;; If the partition is smaller than 3, then pad with \0 to make
      ;; processing conversion rules consistent for each partition
      (concat
        (map #(concat (repeat (- group-size remainder) \0) %)
             (partition-all group-size (take remainder number-string)))
        (partition group-size (drop remainder number-string))))))


(defn character->number-word
  "Convert a sequence of numbers to their word equivalents in a given
  dictionary

  Arguments: hash-map dictionary, vector of strings (representing numbers)
  Return: Sequence of strings (each string is a word representing a number)"

  [dictionary character]

  (get dictionary character))


(defn character-sequence->word-sequence
  "Rules: apply specific look-up for tens and ones combination
  - ten = \0 then lookup digit in digits dictionary (a digit is whole numbers 0-9), dont return anything for tens
  - if ten = \1 then combine ten and digit and lookup in tens dictionary (could refine this around teens)
  - if ten >= \2 then lookup ten in tens dictionary and digit in digits dictionary
  - lookup hundred in digits dictionary and post-fix hundred "
  [dictionary character-sequence]
  (let [[hundred ten digit] character-sequence]

    (cond
      (= \0 ten) (map #(character->number-word dictionary/digit->word %) character-sequence)
      (= \1 ten) (list (character->number-word dictionary/digit->word hundred)
                       (character->number-word dictionary/teens->word (str ten digit)))
      :else      (list (character->number-word dictionary/digit->word hundred)
                       (character->number-word dictionary/tens->word ten)
                       (character->number-word dictionary/digit->word digit)))))



(for [x-y  [[4.6 48.9] [3.5 2.2]]
      :let [co-ord (map str (flatten [4.6 48.9]))]]
  (clojure.string/join ", " co-ord))




(reduce str (clojure.string/join ", " (flatten [4.6 48.9])))
