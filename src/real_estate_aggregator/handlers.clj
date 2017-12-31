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

(defmethod handle-route :listings/index [{:keys [params]}]
  (let [geometry (->> params
                      :geometry
                      (map #(update % 1 read-string))
                      (into {}))]
    (->> (models/retrieve {:dataset :listings, :geometry geometry})
         (map (comp views/listing-marker :search-json))
         r/ok)))
