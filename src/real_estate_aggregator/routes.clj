(ns real-estate-aggregator.routes
  (:require [bidi.bidi :as bidi]))

(def app-routes
  ["/" {"feed" :feed
        "search" :search
        "saved" :saved
        "alerts" :alerts}])

(defmulti url-for (fn [this & _] (type this)))

(defmethod url-for clojure.lang.Keyword [this args]
  (bidi/unmatch-pair app-routes {:handler this :params args}))
