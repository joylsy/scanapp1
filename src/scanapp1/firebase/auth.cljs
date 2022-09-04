(ns scanapp1.firebase.auth
  (:require 
    ["firebase/auth" :refer (getAuth GoogleAuthProvider onAuthStateChanged signInWithPopup signOut)]
    [scanapp1.firebase.db :refer [db-save!]]
    [scanapp1.state :as state]
   ))


;;firebase.auth().onAuthStateChange(function (user))
(defn on-auth-state-changed
  []
  (onAuthStateChanged
   (getAuth)
   (fn [user]
     (if user
       (let [display-name (.-displayName user)
             email (.-email user)
             photo-url (.-photoURL user)
             uid   (.-uid user)]
         (do
           (reset! state/user {:photo-url photo-url
                               :display-name display-name
                               :email email})
           (db-save! ["users" uid "profile"] #js {:display-name display-name
                                                  :email email
                                                  :photo-url photo-url})))
       (reset! state/user nil)))))



