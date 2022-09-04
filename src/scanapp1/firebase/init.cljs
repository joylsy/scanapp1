(ns scanapp1.firebase.init
  (:require ["firebase/app" :refer (initializeApp)]
            [scanapp1.firebase.auth :refer (on-auth-state-changed)]))

(defn firebase-init
  []
  (initializeApp
   #js {:apiKey "AIzaSyASTvylhrQtlLQ92cdtONBSYJmvscmYNBE"
        :authDomain "jlsscanapp.firebaseapp.com"
        :databaseURL "https://jlsscanapp-default-rtdb.asia-southeast1.firebasedatabase.app"
        :projectId "jlsscanapp"
        :storageBucket "jlsscanapp.appspot.com"
        :messagingSenderId "208498127561"
        :appId  "1 :208498127561:web:d7e0e92ece6792bd961318"
        :measurementId "G-3M4DSED8TD"})
  (on-auth-state-changed))



