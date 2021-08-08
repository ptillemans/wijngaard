(ns wijngaard.core
  (:require [reagent.dom :as rdom]
            [reagent.core :as r]
            [wijngaard.firebase.init
             :refer (initialize-firebase)]
            [wijngaard.firebase.firestore :refer (firestore)]
            [wijngaard.firebase.auth :refer (sign-in)]))

(enable-console-print!)


(def plants (r/atom [{:id "1234-abc"
                      :row 1
                      :position 3}]))

(defn set-plants [val]
  (reset! plants val))

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
           (set-plants @result))))))


(defn view []
  [:div "hello world"
   [:ul
    (print @plants)
    (for [plant @plants]
      [:li {:key (:id plant)
            :id (:id plant)}
       (:row plant) " - " (:position plant)])]])

(defn mount-root []
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [sign-in view] root-el)))

(defn ^:export init []
  (print "Initializing-firebase")
  (initialize-firebase)
  (print "Load plants from firestore")
  (load-plants-from-firestore)
  (print "Mounting root")
  (mount-root))

(init)
