(ns wijngaard.queries
  "queries for wijngaard datamodel"
  (:require
   [re-frame.core :as rf]))

;; global state queries
(defn is-initialised? [db _]
  (seq db))

;; authentication queries
(defn is-signed-in?
  [db _]
  (:signed-in db))

;; plant queries
(defn query-plants [db _]
  (:plants db))

(defn init []
  (rf/reg-sub :initialised? is-initialised?)
  (rf/reg-sub :signed-in is-signed-in?)
  (rf/reg-sub :plants query-plants))
