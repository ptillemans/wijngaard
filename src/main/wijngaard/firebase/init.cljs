(ns wijngaard.firebase.init
  (:require ["@firebase/app" :refer (firebase)]))

(def config {:apiKey "AIzaSyCDzl19dPuIbOPtbeweq6rr7r7pHkBZUVY"
             :authDomain "wijngaard-3ca1a.firebaseapp.com"
             :databaseURL "https://wijngaard-3ca1a-default-rtdb.europe-west1.firebasedatabase.app"
             :projectId "wijngaard-3ca1a"
             :storageBucket "wijngaard-3ca1a.appspot.com"
             :messagingSenderId "467742888442"
             :appId "1:467742888442:web:7af3631f659408c1395443"
             :measurementId "G-BQ3GS2L3YW"})

(defn initialize-firebase []
  (print (.-apps firebase))
  (if (empty? (.-apps firebase))
    (do
      (print "initializing ")
      (.initializeApp firebase (clj->js config)))
    (.-app firebase)))
