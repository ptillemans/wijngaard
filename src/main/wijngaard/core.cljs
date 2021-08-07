(ns wijngaard.core
  (:require [reagent.dom :as rdom]))

(enable-console-print!)

(defn view []
  [:div "hello world"])

(defn mount-root []
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render view root-el)))

(defn ^:export init []
  (print "Mounting root")
  (mount-root))

(init)
