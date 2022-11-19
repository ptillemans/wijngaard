(ns wijngaard.types
  (:require
   [malli.core :as m]))

(def StrictPositiveInt
  [:and int? [:> 0]])

(def Plant
  [:map
   [:id string?]
   [:row StrictPositiveInt]
   [:position StrictPositiveInt]])

(defn plant? [plant]
  (m/validate Plant plant))

(comment
  (require '[wijngaard.core :as core])

  (def plant (-> core/initial-state :plants first))

  (m/validate Plant plant)
  )


(def Vineyard
  [:map
   [:id string?]
    [:name string?]
    [:plants [:vector Plant]]])

(defn vineyard? [vineyard]
  (m/validate Vineyard vineyard))
