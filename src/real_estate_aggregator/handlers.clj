(ns real-estate-aggregator.handlers
  (:require [real-estate-aggregator
             [views :as views]
             [models :as models]]
            [ring.util.http-response :as r]))

(defmulti handle-route :handler)

(defmethod handle-route :main [request]
  (views/main-view (assoc request
                          :listings models/listings)))

(defmethod handle-route :listings/show [request]
  (r/ok
    (hiccup.core/html
      (views/listing-page
        (models/query (:route-params request))))))
