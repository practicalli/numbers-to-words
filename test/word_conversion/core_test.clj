(ns word-conversion.core-test
  (:require [clojure.test :refer :all]
            [word-conversion.core :as sut]))



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

  (testing "Convert sequences of numbers to words"
    (is (= ["zero"]         (sut/word-sequence 0)))
    (is (= ["one"]          (sut/word-sequence 1)))
    (is (= ["twenty"]       (sut/word-sequence 20)))
    (is (= ["twenty" "one"] (sut/word-sequence 21)))))

