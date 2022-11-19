(ns wijngaard.app-test
  (:require
   [cljs.test :as t :include-macros true]
   [malli.core :as m]
   [wijngaard.types :as wt]))

(t/deftest plant-test
  (let [plant {:id "1-1" :row 1 :position 1}]
    (t/is (wt/plant? plant))))

(t/deftest vineyard-test
  (let [vineyard {:id "rommelaar" :name "Rommelaereberg" :plants []}]
    (t/is (wt/vineyard? vineyard))))
