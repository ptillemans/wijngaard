(ns wijngaard.core
  (:require [reagent.dom :as rdom]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [wijngaard.firebase.init
             :refer (initialize-firebase)]
            [wijngaard.firebase.auth :as auth]
            [wijngaard.firebase.firestore :as firestore]
            [wijngaard.firebase.auth :refer (sign-in sign-out-view)]))

(enable-console-print!)


(def app-db
  (r/atom
   {:signed-in false
    :plants [{:id "1234-abc"
              :row 1
              :position 3}]}))


(defn query-plants [db _]
  (:plants db))


(defn query-signed-in
  [db _]
  (:signed-in db))



(defn view []
  (let [plants (rf/subscribe [:plants])]
    [:div "hello world"
     [:ul
      (print @plants)
      (for [plant @plants]
        [:li {:key (:id plant)
              :id (:id plant)}
         (:row plant) " - " (:position plant)])]
     [sign-out-view]]))


(defn mount-root []
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [sign-in view] root-el)))


(defn ^:export init []
  (print "Initializing-firebase")
  (initialize-firebase)
  (auth/init)
  (firestore/init)
  (rf/reg-sub :plants query-plants)
  (rf/reg-sub :signed-in query-signed-in)
  (print "Mounting root")
  (mount-root))


(init)
