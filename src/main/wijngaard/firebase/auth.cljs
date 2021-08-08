(ns wijngaard.firebase.auth
  (:require [reagent.core :as r]
            ["react-firebaseui" :refer (StyledFirebaseAuth) ]
            ["@firebase/app" :refer (firebase)]
            ["@firebase/auth" :refer (Auth)]))

(def is-signed-in (r/atom false))

(def ui-config
  (let
      [gp (.. firebase -auth -GoogleAuthProvider)
       google-provider (new gp) ]
   {:signInFlow "redirect"
    :signInOptions [google-provider]}))

(defn sign-in-view
  [auth]
  [:div
   [:h1 "Wijngaard" ]
   [:p "Log in." ]
   [:> StyledFirebaseAuth
    {:uiConfig ui-config
     :firebaseAuth auth}]])


(defn sign-out-view
  [^js/firebase.auth.Auth auth]
  [:a {:on-click #(.signOut auth)} "Log out."])


(defn sign-in
  "Show signin view if not logged in"
  [view]

  (let [auth (.auth firebase)
        unregister (.onAuthStateChanged auth #(reset! is-signed-in true))]
    (r/create-class
     {:display-name "sign in component"
      :component-will-unmount (unregister)
      :reagent-render (fn [view]
                        (print "checking is-signed-in: " @is-signed-in)
                        (if @is-signed-in
                          (view)
                          (sign-in-view auth)))})))
