(ns wijngaard.events
  (:require [re-frame.core :as rf]))

;; global state handler

(defn initial-state-handler [_ [_ initial-state]]
  (print "reset initial state:" initial-state)
  initial-state)

;; events related to authentication

(defn signed-in-handler
  "Handle the signed-in event"
  [{db :db} _]
  (let [new-db (assoc db :signed-in true)]
    (print "signed-in:" db new-db)
    {:db new-db
     :persistence :load-plants}))

(defn signed-out-handler
  [{db :db} _]
  (let [new-db (assoc db :signed-in false)]
    {:db new-db
     :auth :sign-out}))

;; events related to plants

(defn process-plants-handler
  [db [_ plants]]
  (print "process plants:" plants)
  (assoc db :plants plants))

(defn add-plant-handler
  [{db :db} plant]
  (let [new-db (update-in db [:plants] conj plant)]
    {:db new-db
     :persistent [:save-plant plant]}))

(defn init []
  (print "registering events")
  (rf/reg-event-fx :signed-in signed-in-handler)
  (rf/reg-event-fx :signed-out signed-out-handler)
  (rf/reg-event-db :process-plants process-plants-handler)
  (rf/reg-event-fx :add-plant add-plant-handler)
  (rf/reg-event-db :initialize-state initial-state-handler))
