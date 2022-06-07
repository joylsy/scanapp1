(ns scanapp1.views
  (:require
   [re-frame.core :as re-frame]
   [scanapp1.events :as events]   
   [scanapp1.subs :as subs]
   ))


(defn text-input [id label]
  (let [value (re-frame/subscribe [::subs/form id])]
    [:div.field
     [:label.label label]
     [:div.control
      [:input.input {:value @value
                     :on-change #(re-frame/dispatch [::events/update-form id (-> % .-target .-value)])
                     :type "text" :placeholder ""}]]]))

(def locations ["Store" "WareHouse 1" "WareHouse 2"])

(defn select-input [id label options]
  (let [value (re-frame/subscribe [::subs/form id])]
    [:div.field
     [:label.label label]
     [:div.control
      [:div.select
       [:select {:value @value :on-change #(re-frame/dispatch [::events/update-form id (-> % .-target .-value)])}
        [:option {:value ""} "Please select"]
        (map (fn [o] [:option {:key o :value o} o]) options)]]]]))

(defn main-panel []
  (let [is-valid? @(re-frame/subscribe [::subs/form-is-valid? [:location :barcode]])]
    [:div.section
     [select-input :location "Location" locations]     
     [text-input :barcode "Barcode"]
     [:button.button.is-primary {:disabled (not is-valid?)
                                 :on-click #(re-frame/dispatch [::events/save-form])} "Save"]]))
