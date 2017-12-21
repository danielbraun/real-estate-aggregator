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

#_(defn m [handler]
  (fn [request]
    (let [{:keys [body] :as response} (handler request)]
      (if (and (= :html (:mime (:accept request))) (map? body))
        (assoc response :body (-> body
                                  ))
        ))
    ))

(def handler
  (-> handlers/handle-route
      (wrap-bidi-routing routes/app-routes)
      wrap-compojure-render
      (ring.middleware.accept/wrap-accept
        {:mime ["text/html" :as :html]})
      (defaults/wrap-defaults defaults/site-defaults)))
