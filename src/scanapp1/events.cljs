(ns scanapp1.events
  (:require
   [re-frame.core :as re-frame]
   [scanapp1.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))


(re-frame/reg-event-db
 ::update-form
 (fn [db [_ id val]]
   (assoc-in db [:form id] val)))

(re-frame/reg-event-db
 ::save-form
 (fn [db]
   (let [form-data (:form db)
         loc-barcodes (get db :loc-barcodes [])
         updated-loc-barcodes (conj loc-barcodes form-data)]
     (-> db
         (assoc :loc-barcodes updated-loc-barcodes)
         (dissoc :form)))))