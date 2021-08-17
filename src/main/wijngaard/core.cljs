(ns wijngaard.core
  (:require [reagent.dom :as rdom]
            [reagent.core :as r]
            [re-frame.core :as rf]
            [wijngaard.firebase.init
             :refer (initialize-firebase)]
            [wijngaard.firebase.firestore :refer (firestore)]
            [wijngaard.firebase.auth :refer (sign-in)]))

(enable-console-print!)


(def app-db
  (r/atom
   {:signed-in false
    :plants [{:id "1234-abc"
              :row 1
              :position 3}]}))

(defn load-plants-from-firestore []
  (-> (firestore)
      (.collection "plants")
      (.get)
      (.then
       (fn [snapshot]
         (let [result (r/atom [])]
           (.forEach snapshot
                     (fn [doc]
                       (swap! result conj
                              (conj {:id (.-id doc)}
                                    (js->clj (.data doc) :keywordize-keys true)))))
           (rf/dispatch @result))))))

(defn firebase-effects [_]
  (load-plants-from-firestore))



(defn query-plants [db _]
  (:plants db))



(defn view []
  (let [plants (rf/subscribe [:plants])]
    [:div "hello world"
     [:ul
      (print @plants)
      (for [plant @plants]
        [:li {:key (:id plant)
              :id (:id plant)}
         (:row plant) " - " (:position plant)])]]))

(defn mount-root []
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [sign-in view] root-el)))

(defn ^:export init []
  (print "Initializing-firebase")
  (initialize-firebase)
  (print "Mounting root")
  (rf/reg-fx :firebase firebase-effects)
  (rf/reg-sub :plants query-plants)
  (mount-root))

(init)
