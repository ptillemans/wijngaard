(ns wijngaard.build
  (:require
   [shadow.cljs.devtools.api :as shadow]
   [clojure.java.shell :refer (sh)]))

(defn deploy []
  (shadow/release :app)
  (sh "firebase" "deploy"))
