(ns wijngaard.firebase.firestore
  (:require ["@firebase/app" :refer (firebase)]
            ["@firebase/firestore"]
            [re-frame.core :as rf]))

(defn firestore []
  (.firestore firebase))

(defn doc-to-plant [doc]
  (let [plant
        (conj {:id (.-id doc)}
              (js->clj (.data doc) :keywordize-keys true))]
    (print "doc-to-plant: " plant)
    plant))

(defn dispatch-plants [snapshot]
  (print "dispatch plants:")
  (let [plants (atom ())]
    (.forEach snapshot
              #(swap! plants conj (doc-to-plant %)))
    (print "results : " @plants)
    (rf/dispatch [:process-plants (vec @plants)])))

(defn load-plants []
  (print "Loading plants...")
  (-> (firestore)
      (.collection "plants")
      (.get)
      (.then dispatch-plants)
      (.catch #(print "error loading plants: " %))))

(defn firestore-fx [_]
  (load-plants))

(defn init []
  (rf/reg-fx :persistence firestore-fx))
