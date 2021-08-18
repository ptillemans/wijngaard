(ns wijngaard.firebase.auth
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            ["react-firebaseui" :refer (StyledFirebaseAuth) ]
            ["@firebase/app" :refer (firebase)]
            ["@firebase/auth"]))

(defn google-auth-provider-id []
  (.. firebase -auth -GoogleAuthProvider -PROVIDER_ID))

(defn ui-config []
   {:signInFlow "popup"
    :signInOptions #js [ (google-auth-provider-id)  ]
    :callbacks {
                :signInSuccessWithAuthResult
                (fn []
                  (print "dispatch sign-in")
                  (rf/dispatch [:signed-in])
                  false)
                }
    })

(defn sign-in-view []
  [:div
   [:h1 "Wijngaard" ]
   [:p "Log in." ]
   [:> StyledFirebaseAuth
    {:uiConfig (ui-config)
     :firebaseAuth (.auth firebase)
     }]])

(defn signed-in-handler
  "Handle the signed-in event"
  [{db :db} _]
  (let [new-db (assoc db :signed-in true)]
    (print "signed-in:" db new-db)
    {:db new-db
     :persistence :load-plants }))

(defn signed-out-handler
  [{db :db} _]
  (let [auth (.auth firebase)
        new-db (assoc db :signed-in false) ]
    {:db new-db
     :auth :sign-out}))

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
  (print "registering auth events and effects")
  (rf/reg-event-fx :signed-in signed-in-handler)
  (rf/reg-event-fx :signed-out signed-out-handler)
  (rf/reg-fx :auth auth-fx))
