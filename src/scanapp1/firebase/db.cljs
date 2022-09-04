(ns scanapp1.firebase.db
  (:require ["firebase/database" :refer (getDatabase onValue ref set push)] 
            [scanapp1.state :as state]
            [re-frame.core :as rf]
            [reagent.core :as r]
            [reagent.ratom :as ratom]
            [cljs-bean.core :refer [->js ->clj]]
            [clojure.string :as string]))

(rf/reg-event-fx
 ::firebase-error
 (fn [_ [_ error]]
   (js/console.error (str "error:\n" error))))

(rf/reg-event-fx
 ::firebase-success
 (fn [_ [_]]
   (js/console.log (str "Write Succeeded"))))

(def default-pass-fail
  {:on-success [::firebase-success]
   :on-failure [::firebase-error]})

(defn success-failure-dispatch [args]
  (let [{:keys [on-success on-failure]} (merge default-pass-fail args)]
    (fn [err]
      (rf/dispatch
       (if (nil? err)
         on-success
         on-failure)))))

;; database().ref('users/' + userId)
(defn db-ref
  [path]  ;; ["users" userId "pathname"]
  (let [db (getDatabase)]
    (ref db (string/join "/" path))))

(defn db-save!
  [path value]
  (set (db-ref path) value))

;; (defn db-subscribe
;;   [path]
;;   (onValue (db-ref path) (fn [^js snapshot]
;;                            (reset! state/connected
;;                                    (js->clj (.val snapshot) :keywordize-keys true)))))


(defn ->path [p]
  (string/join "/" (->js p)))


(defn database-ref1
  [path]  ;; ["users" userId "pathname"]
  (let [db (getDatabase)
        jpath (->path path)]
    (js/console.log  jpath)
    (ref db jpath)))

(defn- ref-set [{:keys [path value] :as args}]
  (set (database-ref1 path)
       (->js value)
       (success-failure-dispatch args)))

(defn get-push-key1 [path]
  (let [push-key (-> (database-ref1 path)
                     (push)
                     (.-key))]
    (js/console.log  push-key)
    (concat path [push-key])))


(rf/reg-fx ::push-fx
           (fn [args]
             (ref-set
              (-> args
                  (update :path get-push-key1)))))

(rf/reg-event-fx
 ::push
 (fn [_ [_ args]]
   {::push-fx args}))

(defn on-value-reaction
  "returns a reagent atom that will always have the latest value at 'path' in the Firebase database"
  [{:keys [path] :as args}]
  (let [ref (database-ref1 path)
        reaction (r/atom args)
        callback (fn [^js x] (reset! reaction (some-> x (.val) ->clj)))]
    (onValue ref callback)
    (ratom/make-reaction
     (fn [] @reaction))))

(rf/reg-sub ::realtime-value
            (fn [[_ args]]
              (on-value-reaction args))
            identity)
