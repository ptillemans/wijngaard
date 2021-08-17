(ns wijngaard.firebase.auth
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            ["react-firebaseui" :refer (StyledFirebaseAuth) ]
            ["@firebase/app" :refer (firebase)]
            ["@firebase/auth" :refer (Auth)]))

(def ui-config
   {:signInFlow "popup"
    :signInOptions
    #js [ (.. firebase -auth -GoogleAuthProvider -PROVIDER_ID) ]
    :callbacks {
                :signInSuccessWithAuthResult
                (fn []
                  (print "dispatch sign-in")
                  (rf/dispatch [:signed-in])
                  false)
                }
    })

(defn sign-in-view
  [auth]
  (let [provider (.. firebase -auth -GoogleAuthProvider -PROVIDER_ID)]
    (print "sign-in-view: " provider)
    [:div
     [:h1 "Wijngaard" ]
     [:p "Log in." ]
     [:> StyledFirebaseAuth
      {:uiConfig ui-config
       :firebaseAuth auth
       }]]))

(defn sign-in-handler
  "Handle the signed-in event"
  [{:keys [db]} _]
  (let [new-db (assoc db :signed-in true)]
    {:db new-db
     :firebase :load-plants }))

(rf/reg-event-fx :signed-in sign-in-handler)

(defn query-signed-in
  [db]
  (:signed-in db))

(rf/reg-sub :signed-in query-signed-in)

(defn sign-out-view
  [^js/firebase.auth.Auth auth]
  [:a {:on-click #(.signOut auth)} "Log out."])

(defn sign-in
  "Show signin view if not logged in"
  [view]

  (let [auth (.auth firebase)
        is-signed-in (rf/subscribe [:signed-in])]
    (r/create-class
     {:display-name "sign in component"
      :reagent-render (fn [view]
                        (print "checking is-signed-in: " @is-signed-in)
                        (if @is-signed-in
                          (view)
                          (sign-in-view auth)))})))
