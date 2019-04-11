(ns word-conversion.core-test
  (:require [clojure.test :refer :all]
            [word-conversion.core :as sut]
            [word-conversion.dictionaries :refer [british-english-dictionary]]))

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

