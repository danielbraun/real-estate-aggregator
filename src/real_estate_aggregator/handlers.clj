(ns real-estate-aggregator.handlers
  (:require [real-estate-aggregator
             [views :as views]
             [models :as models]]))

(defmulti handle-route :handler)

(defmethod handle-route :main [request]
  (views/main-view (assoc request
                          :listings models/listings)))
