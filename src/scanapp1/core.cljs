(ns scanapp1.core
  (:require
   [reagent.dom :as rdom]
   [re-frame.core :as re-frame]
   [scanapp1.events :as events]
   [scanapp1.views :as views] 
   [scanapp1.config :as config]
   [scanapp1.firebase.init :refer [firebase-init]]
   ))


(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (re-frame/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))

(defn init []
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (firebase-init)
  (mount-root))
