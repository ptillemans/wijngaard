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


(def initial-state
  {:signed-in false
   :plants [{:id "1234-abc"
             :row 1
             :position 3}]})


(defn query-plants [db _]
  (:plants db))


(defn query-signed-in
  [db _]
  (:signed-in db))

(defn process-plants-handler
  [{db :db} [_ plants]]
  (print "process plants:" plants)
  {:db (assoc db :plants plants)})

(defn initial-state-handler [_ _]
  (print "reset initial state:" initial-state)
  {:db initial-state})

(defn view []
  (let [plants (rf/subscribe [:plants])]
    [:div "hello world"
     [:ul
      (for [plant @plants]
        [:li {:key (:id plant)
              :id (:id plant)}
         (:row plant) " - " (:position plant)])]
     [sign-out-view]]))

(defn top-panel    ;; this is new
  []
  (let [ready?  (re-frame.core/subscribe [:initialised?])]
    (if-not @ready?         ;; do we have good data?
      [:div "Initialising ..."]   ;; tell them we are working on it
      [sign-in view])))      ;; all good, render this component

(defn mount-root []
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [top-panel] root-el)))


(defn ^:export init []
  (print "Initializing-firebase")
  (initialize-firebase)
  (auth/init)
  (firestore/init)
  (rf/reg-event-fx :process-plants process-plants-handler)
  (rf/reg-event-fx :initialize-state initial-state-handler)
  (rf/reg-sub :plants query-plants)
  (rf/reg-sub :signed-in query-signed-in)
  (re-frame.core/reg-sub   ;; we can check if there is data
   :initialised?          ;; usage (subscribe [:initialised?])
   (fn  [db _]
     (not (empty? db))))  ;; do we have data
  (rf/dispatch [:initialize-state])
  (print "Mounting root")
  (mount-root)
  (print "init done"))


(init)
