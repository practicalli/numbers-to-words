(ns word-conversion.core-test
  (:require [clojure.test :refer :all]
            [word-conversion.core :as sut]
            [word-conversion.dictionaries :refer [british-english-dictionary]]))



(deftest number->british-english-test
  "Test the conversion of numerical whole numbers to
   strings containing British English words as a sentence"

  (testing "Sample data tests - single values"
    (is (= "zero" (sut/digit->word 0 sut/british-english-numbers)))
    (is (= "one"  (sut/digit->word 1 sut/british-english-numbers))))

  (testing "Sample data tests - double figure values"
    (is (= "20" (sut/positional-number-string "21"))))

  (testing "Generating sequences of numbers with positional number level"
    (is (= ["20" "1"]   (sut/number-sequence 21)))
    (is (= ["2000"]     (sut/number-sequence 2000)))
    (is (= ["2000" "1"] (sut/number-sequence 2001))))

  ;; 2001 probably should be "2" "x000" "1"


  (testing "Convert sequences of numbers to words"
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

  (testing "Parsing a number into a sequence of numbers"
    (is (= ["0"],
           (sut/parse-number ["0"])))
    (is (= ["20" "1"],
           (sut/parse-number ["21"])))
    (is (= ["1" "x00"],
           (sut/parse-number ["100"])))
    (is (= ["10" "x000" "5" "x00" "40" "5"],
           (sut/parse-number ["10545"])))))



;; Representative test cases
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
