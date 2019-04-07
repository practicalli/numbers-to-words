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
    (is (= [20 1] (sut/positional-numbers 21)))))
