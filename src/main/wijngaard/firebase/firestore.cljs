(ns wijngaard.firebase.firestore
  (:require ["@firebase/app" :refer (firebase)]
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

(defn add-plant [plant]
  (print "Adding plant " plant)
  (-> (firestore)
      (.collection "plants")
      (.add (clj->js plant))
      (.catch #(print "error adding plant: " %))))

(defn firestore-fx [cmd arg]
  (case cmd
    :load-plants (load-plants)
    :add-plant (add-plant arg)))

(defn init []
  (rf/reg-fx :persistence firestore-fx))

(comment

  (defn create-vineyard [nr-rows nr-plants]
    (for [row (range 1 nr-rows)
          pos (range 1 nr-plants)]
      (let [plant {:row row :position pos}]
        (add-plant plant)))))
