(ns main.wijngaard.types
  (:require
   [malli.core :as m]))

(def StrictPositiveInt
  [:and int? [:> 0]])

(def Plant
  [:map
   [:id string?]
   [:row StrictPositiveInt]
   [:position StrictPositiveInt]])


(comment
  (require '[wijngaard.core :as core])

  (def plant (-> core/initial-state :plants first))

  (m/validate Plant plant)
  )
