(ns word-conversion.core-test
  (:require [clojure.test :refer :all]
            [word-conversion.core :as sut]))



(deftest number->british-english-test
  "Test the conversion of numerical whole numbers to
   strings containing British English words as a sentence"

  (testing "Sample data tests"
    (is (= (sut/number->word 0) "zero"))))
