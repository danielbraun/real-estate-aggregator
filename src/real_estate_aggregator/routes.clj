(ns real-estate-aggregator.routes
  (:require [bidi.bidi :as bidi]
            [bidi-rest.core :refer [resources]]))

(def app-routes
  ["/" [["" :main]
        (resources :listings)]])

(defmulti url-for (fn [this & _] (type this)))

(defmethod url-for clojure.lang.Keyword [this args]
  (bidi/unmatch-pair app-routes {:handler this :params args}))
