(ns real-estate-aggregator.core
  (:require bidi.bidi
            (ring.middleware [defaults :as defaults]
                             [format :as format]
                             accept)
            (real-estate-aggregator [routes :as routes]
                                    [views :as views]
                                    [handlers :as handlers])
            compojure.response))

(defn- wrap-bidi-routing [handler routes]
  (fn [request]
    (handler (bidi.bidi/match-route* routes (:uri request) request))))

(defn- wrap-compojure-render [handler]
  (fn [request]
    (compojure.response/render (handler request) request)))

(def handler
  (-> handlers/handle-route
      (wrap-bidi-routing routes/app-routes)
      wrap-compojure-render
      (ring.middleware.accept/wrap-accept
        {:mime ["text/html" :as :html]})
      format/wrap-restful-format
      (defaults/wrap-defaults defaults/site-defaults)))
