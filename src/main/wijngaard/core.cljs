(ns wijngaard.core
  (:require
   [re-frame.core :as rf]
   [reagent.dom :as rdom]
   [wijngaard.events :as events]
   [wijngaard.firebase.auth :as auth]
   [wijngaard.firebase.firestore :as firestore]
   [wijngaard.firebase.init :refer (initialize-firebase)]
   [wijngaard.queries :as queries]))

(enable-console-print!)

(def initial-state
  {:signed-in false
   :plants [{:id "1234-abc"
             :row 1
             :position 3}]})

(defn view []
  (let [plants (rf/subscribe [:plants])]
    [:div "hello world"
     [:ul
      (for [plant @plants]
        [:li {:key (:id plant)
              :id (:id plant)}
         (:row plant) " - " (:position plant)])]
     [auth/sign-out-view]]))

(defn top-panel    ;; this is new
  []
  (let [ready?  (re-frame.core/subscribe [:initialised?])]
    (if-not @ready?         ;; do we have good data?
      [:div "Initialising ..."]   ;; tell them we are working on it
      [auth/sign-in view])))      ;; all good, render this component

(defn mount-root []
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [top-panel] root-el)))

(defn ^:export init []
  (initialize-firebase)
  (auth/init)
  (firestore/init)
  (events/init)
  (queries/init)
  (rf/dispatch [:initialize-state initial-state])
  (print "Mounting root")
  (mount-root)
  (print "init done"))

(init)
