(ns real-estate-aggregator.models
  (:require [clj-http.client :as http]
            [yad2-api.mobile :as yad2]))

(def listings
  (->> [1 2 3]
       (pmap #(yad2/retrieve-search-results {:cat 2 :subcat %}))
       (map :feed_items)
       (apply concat)
       (filter :id)
       (filter (comp #{"standard"} :ad_type))))

(comment
  (clojure.pprint/pprint (sort(first listings))))

(defn query [{:keys [id]}]
  (cond
    id (yad2/retrieve-ad {:id id})
    :else nil))
