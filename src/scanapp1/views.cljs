(ns scanapp1.views
  (:require
   [re-frame.core :as re-frame]
   [scanapp1.events :as events]   
   [scanapp1.subs :as subs]
   ))

(defn loc-barcodes-list []
(let [loc-barcodes1 @(re-frame/subscribe [::subs/barcodes])]
  [:div
   [:h1 {:class "title is-4"} "Encoded Barcodes"]
   [:ul 
    (map (fn [{:keys [location barcode]}]
           [:li {:key barcode} (str location " (" barcode ")" )]) loc-barcodes1)]]))


(defn text-input [id label]
  (let [value (re-frame/subscribe [::subs/form id])]
    [:div.field
     [:label.label label]
     [:div.control
      [:input.input {:value @value
                     :on-change #(re-frame/dispatch [::events/update-form id (-> % .-target .-value)])
                     :type "text" :placeholder ""}]]]))

(defn text-input-add [id label]
  (let [value (re-frame/subscribe [::subs/form id])]
    [:div.field.has-addons
     [:div.control
      [:input       {:class "input"
                     :value @value
                     :on-change #(re-frame/dispatch [::events/update-form id (-> % .-target .-value)])
                     :type "text" :placeholder ""}]]
     [:div.control
       [:a.button.is-primary {:on-click #(re-frame/dispatch [::events/save-form])} "Add" ]]]))

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
    [:div.columns.is-mobile.is-multiline.is-centered 
     [:div.column
      [select-input :location "Location" locations]
      [text-input-add :barcode "Barcode"]]
     [:div.column
      [loc-barcodes-list]]])     
     
     
