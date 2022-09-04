(ns scanapp1.state
  (:require [reagent.core :as r]))

(def user (r/atom nil))

(def connected (r/atom nil))