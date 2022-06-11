(ns scanapp1.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::form
 (fn [db [_ id]]
   (get-in db [:form id] "")))

(re-frame/reg-sub
 ::form-is-valid?
 (fn [db [_ form-ids]]
   (every? #(get-in db [:form %]) form-ids)))

(re-frame/reg-sub
 ::barcodes
 (fn [db]
   (get db :loc-barcodes [])))
