(ns wijngaard.firebase.firestore
  (:require ["@firebase/app" :refer (firebase)]
            ["@firebase/firestore"]))

(defn firestore []
  (.firestore firebase))
