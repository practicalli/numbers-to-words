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
    (is (= '((\0 \0 \0))
           (sut/partition-number-string "0"))
        "Testing number scales - one")
    (is (= '((\0 \2 \1))
           (sut/partition-number-string "21"))
        "Testing number scales - ten")
    (is (= '((\3 \2 \1))
           (sut/partition-number-string "321"))
        "Testing number scales - hundred")
    (is (= '((\0 \0 \4) (\3 \2 \1))
           (sut/partition-number-string "4321"))
        "Testing number scales - thousand")
    (is (= '((\0 \5 \4) (\3 \2 \1))
           (sut/partition-number-string "54321"))
        "Testing number scales - ten thousand")
    (is (= '((\6 \5 \4) (\3 \2 \1))
           (sut/partition-number-string "654321"))
        "Testing number scales - hundred thousand")
    (is (= '((\0 \0 \7) (\6 \5 \4) (\3 \2 \1))
           (sut/partition-number-string "7654321"))
        "Testing number scales - million")
    (is (= '((\0 \8 \7) (\6 \5 \4) (\3 \2 \1))
           (sut/partition-number-string "87654321"))
        "Testing number scales - ten million")
    (is (= '((\9 \8 \7) (\6 \5 \4) (\3 \2 \1))
           (sut/partition-number-string "987654321"))
        "Testing number scales - hundred million")
    (is (= '((\1 \0 \0) (\0 \0 \0) (\0 \0 \0))
           (sut/partition-number-string "100000000"))
        "Testing number scales - billion")
    )
  ;; Move defensive programming higher up the function chain
  #_(testing "Defensive programming tests"
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
    (is (= '("zero" "zero" "zero")
           (sut/character-sequence->word-sequence dictionary/digit->word '(\0 \0 \0))))
    (is (= '("zero" "zero" "one")
           (sut/character-sequence->word-sequence dictionary/digit->word '(\0 \0 \1)))))

  (testing "Tens to number words"
    (is (= '("zero" "ten")
           (sut/character-sequence->word-sequence dictionary/digit->word '(\0 \1 \0))))
    (is (= '("zero" "eleven")
           (sut/character-sequence->word-sequence dictionary/digit->word '(\0 \1 \1))))
    (is (= '("zero" "twenty" "zero")
           (sut/character-sequence->word-sequence dictionary/digit->word '(\0 \2 \0))))
    (is (= '("zero" "twenty""one")
           (sut/character-sequence->word-sequence dictionary/digit->word '(\0 \2 \1))))
    (is (= '("zero" "forty" "two")
           (sut/character-sequence->word-sequence dictionary/digit->word '(\0 \4 \2)))))

  (testing "Hundreds to number words"
    (is (= '("one" "zero" "five")
           (sut/character-sequence->word-sequence dictionary/digit->word '(\1 \0 \5))))
    (is (= '("one" "twenty" "three")
           (sut/character-sequence->word-sequence dictionary/digit->word '(\1 \2 \3))))))






(deftest development-unit-tests
  "Tests created to develop the solution.
  These are testing the actual implementation of the algorithms uses,
  so should eventually be replaced by namespace api tests which test
  the intent and context of the namespace and not a specific implementation."

  (partition-number-string-test)
  (character->number-word-test)
  (character-sequence->word-sequence-test))



#_(testing "Thousands to number words"
    (is (= "one thousand and five"
           (sut/character-sequence->word-sequence dictionary/digit->word 1005)))
    (is (= "one thousand and forty two"
           (sut/character-sequence->word-sequence dictionary/digit->word 1042)))
    (is (= "one thousand one hundred and five"
           (sut/character-sequence->word-sequence dictionary/digit->word 1105))))

#_(testing "Millions to number words"
    (is (= "fifty six million nine hundred and fourty five thousand seven hundred and eighty one"
           (sut/character-sequence->word-sequence dictionary/digit->word 56945781)))
    (is (= "nine hundred and ninety nine million nine hundred and ninety nine thousand
             nine hundred and ninety nine"
           (sut/character-sequence->word-sequence dictionary/digit->word 999999999))))




#_(deftest cheque-numbers->words-test
  "Converting numbers on a cheque to words"

 (testing "convert-cheque simple number conversion"
   (is (= "seventy-nine pounds and six pence"
          (sut/convert-cheque 79.06))) ))



#_(deftest number->british-english-test
    "Test the conversion of numerical whole numbers to
   strings containing British English words as a sentence"

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


;; Sample data test cases
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; |     Input | Output                                                                                                   |
;; |-----------|----------------------------------------------------------------------------------------------------------|
;; |         0 | zero                                                                                                     |
;; |         1 | one                                                                                                      |
;; |        21 | twenty  one                                                                                              |
;; |       105 | one hundred  and five                                                                                    |
;; |       123 | one hundred and twenty three                                                                             |
;; |      1005 | one thousand and five                                                                                    |
;; |      1042 | one thousand and forty two                                                                               |
;; |      1105 | one thousand one hundred and five                                                                        |
;; |  56945781 | fifty six million nine hundred and forty five thousand seven hundred and eighty one                      |
;; | 999999999 | nine hundred and ninety nine million nine hundred and ninety nine thousand nine hundred and ninety nine |
