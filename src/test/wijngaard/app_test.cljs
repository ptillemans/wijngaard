(ns wijngaard.app-test
  (:require
   [cljs.test :as t :include-macros true]
   [malli.core :as m]))

(t/deftest first-test
  (t/is (= 1 0)))

(t/deftest second-test
  (t/is (= 1 1)))
