(ns wijngaard.firebase.auth
  "implement authorisation service using firebase"
  (:require
   ["@firebase/app" :refer (firebase)]
   ["@firebase/auth"]
   ["react-firebaseui" :refer (StyledFirebaseAuth)]
   [re-frame.core :as rf]))

(defn google-auth-provider-id []
  (.. firebase -auth -GoogleAuthProvider -PROVIDER_ID))

(defn email-auth-provider-id []
  (.. firebase -auth -EmailAuthProvider -PROVIDER_ID))

(defn ui-config []
  {:signInFlow "popup"
   :signInOptions #js [(google-auth-provider-id)]
   :callbacks {:signInSuccessWithAuthResult
               (fn []
                 (print "dispatch sign-in")
                 (rf/dispatch [:signed-in])
                 false)}})

(defn sign-in-view []
  [:div
   [:h1 "Wijngaard"]
   [:p "Log in."]
   [:> StyledFirebaseAuth
    {:uiConfig (ui-config)
     :firebaseAuth (.auth firebase)}]])

(defn auth-fx [_]
  (-> firebase
      (.auth)
      (.signOut)
      (.then #(print "signed out"))
      (.catch #(print "error signing out"))))

(defn sign-out-view []
  [:a {:on-click #(rf/dispatch [:signed-out])} "Log out."])

(defn sign-in
  "Show signin view if not logged in"
  [view]

  (let [is-signed-in (rf/subscribe [:signed-in])]
    (print "checking is-signed-in: " @is-signed-in)
    (if @is-signed-in
      (view)
      (sign-in-view))))

(defn init []
  (rf/reg-fx :auth auth-fx))
