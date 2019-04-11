(ns word-conversion.core-test
  (:require [clojure.test :refer :all]
            [word-conversion.core :as sut]
            [word-conversion.dictionaries :as dictionary]))

;; Testing approach
;; `deftest` each public function in a namespace
;; `deftest-` each function that will be private (development only)
;; `testing` different aspects of a specific function
;; `is` assertion with intent in string


(deftest- partition-number-string-test
  "Convert a number string into groups of three, from right hand side,
   to model the structure of English words that represent numbers"

  (testing "Convert string number to a 3 digit character sequence,
            along the number levels for thousand, million, billion, etc."
    (is (= '((\0))
           (sut/partition-number-string "0"))
        "Testing number scales - one")
    (is (= '((\2 \1))
           (sut/partition-number-string "21"))
        "Testing number scales - ten")
    (is (= '((\3 \2 \1))
           (sut/partition-number-string "321"))
        "Testing number scales - hundred")
    (is (= '((\4) (\3 \2 \1))
           (sut/partition-number-string "4321"))
        "Testing number scales - thousand")
    (is (= '((\5 \4) (\3 \2 \1))
           (sut/partition-number-string "54321"))
        "Testing number scales - ten thousand")
    (is (= '((\6 \5 \4) (\3 \2 \1))
           (sut/partition-number-string "654321"))
        "Testing number scales - hundred thousand")
    (is (= '((\7) (\6 \5 \4) (\3 \2 \1))
           (sut/partition-number-string "7654321"))
        "Testing number scales - million")
    (is (= '((\8 \7) (\6 \5 \4) (\3 \2 \1))
           (sut/partition-number-string "87654321"))
        "Testing number scales - ten million")
    (is (= '((\9 \8 \7) (\6 \5 \4) (\3 \2 \1))
           (sut/partition-number-string "987654321"))
        "Testing number scales - hundred million")
    (is (= '((\1 \0 \0) (\0 \0 \0) (\0 \0 \0))
           (sut/partition-number-string "100000000"))
        "Testing number scales - billion")
    )
  (testing "Defensive programming tests"
    (is (thrown? java.lang.AssertionError
           (sut/partition-number-string "-1"))
        "Defensive: checking for out of bounds handling")))

(deftest- character->number-word-test
  (testing "Edge cases"
    (is (= "zero"
           (sut/character->number-word dictionary/digit->word \0))
        "Edge case - lower bound test for single character")
    (is (= "nine"
           (sut/character->number-word dictionary/digit->word \9))
        "Edge case - upper bound test for single character")))


(deftest- character-sequence->word-sequence-test
    "Test the conversion of a sequence of characters (\3 \2 \1) to
   a sequence of strings of number words (three twenty one)."

  (testing "Digits to number words"
    (is (= '("zero")
           (sut/character-sequence->word-sequence dictionary/digit->word '(\0))))
    (is (= '("one")
           (sut/character-sequence->word-sequence dictionary/digit->word '(\1)))))

  (testing "Tens to number words"
    (is (= '("ten")
           (sut/character-sequence->word-sequence dictionary/digit->word '(\1 \0))))
    (is (= '("eleven")
           (sut/character-sequence->word-sequence dictionary/digit->word '(\1 \1))))
    (is (= '("twenty")
           (sut/character-sequence->word-sequence dictionary/digit->word '(\2 \0))))
    (is (= '("twenty""one")
           (sut/character-sequence->word-sequence dictionary/digit->word '(\2 \1))))
    (is (= '("forty" "two")
           (sut/character-sequence->word-sequence dictionary/digit->word '(\4 \2)))))

  (testing "Hundreds to number words"
    (is (= '("one" "five")
           (sut/character-sequence->word-sequence dictionary/digit->word 105)))
    (is (= '("one" "twenty" "three")
           (sut/character-sequence->word-sequence dictionary/digit->word 123)))))



(deftest development-unit-tests
  "Tests created to develop the solution.
  These are testing the actual implementation of the algorithms uses,
  so should eventually be replaced by namespace api tests which test
  the intent and context of the namespace and not a specific implementation."

  (partition-number-string-test)
  (character->number-word-test))

