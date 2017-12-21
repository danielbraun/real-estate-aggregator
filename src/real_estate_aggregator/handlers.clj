(ns real-estate-aggregator.handlers
  (:require [real-estate-aggregator.views :as views]))

(defmulti handle-route :handler)

(defmethod handle-route :search [request]
  (views/main-view request)
  )
