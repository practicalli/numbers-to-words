(ns word-conversion.core
  (:require [word-conversion.dictionaries :as dictionary]))



(defn character->number-word
  " "
  [dictionary character]

  character)

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

  {:pre [(<= 0 (Integer. number-string))]}

  (let [group-size  3
        number-size (count number-string)
        quotient    (quot number-size group-size)
        remainder   (rem number-size group-size)]

    (if (= 0 remainder)
      (partition group-size number-string)
      (concat
        (partition-all group-size (take remainder number-string))
        (partition group-size (drop remainder number-string))))))


