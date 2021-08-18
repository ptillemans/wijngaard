(ns wijngaard.firebase.firestore
  (:require ["@firebase/app" :refer (firebase)]
            ["@firebase/firestore"]
            [re-frame.core :as rf]))

(defn firestore []
  (.firestore firebase))

(defn doc-to-plant [doc]
  (conj {:id (.-id doc)}
        (js->clj (.data doc) :keywordize-keys)))

(defn load-plants []
  (print "Loading plants...")
  (-> (firestore)
      (.collection "plants")
      (.get)
      (.then #(rf/dispatch (map doc-to-plant %)))))

(defn firestore-effects [_]
  (load-plants))

(defn init []
  (rf/reg-fx :firebase firestore-effects))
