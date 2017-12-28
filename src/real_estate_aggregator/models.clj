(ns real-estate-aggregator.models
  (:require [clj-http.client :as http]
            [real-estate-aggregator.db :as db]
            [yad2-api.mobile :as yad2]
            [clj-postgresql.spatial :as st]))

(def listings
  (->> [1 2 3]
       (pmap #(yad2/retrieve-search-results {:cat 2 :subcat %}))
       (map :feed_items)
       (apply concat)
       (filter :id)
       (filter (comp #{"standard"} :ad_type))))

(defn query [{:keys [id]}]
  (cond
    id (yad2/retrieve-ad {:id id})
    :else nil))

(def retrieve-search-results
  (memoize yad2/retrieve-search-results))

(defn- to-row [{:keys [id] :as ad}]
  {:yad2-listing/id id
   :yad2-listing/search_json (pr-str ad)})

(defn- all-subcat-results [subcat]
  (let [fetch #(retrieve-search-results {:cat 2 :subcat subcat :page %})
        {:keys [total_pages]} (fetch 1)]
    (->> total_pages
         inc
         (range 1)
         (pmap fetch)
         (map :feed_items)
         (apply concat)
         (filter :id))))

(defn- insert-listings! [rows]
  (-> {:insert-into :listings
       :values (map to-row rows)
       :upsert {:on-conflict [:yad2-listing/id]
                :do-update-set [:yad2-listing/search-json]}}
      db/sql
      db/execute!))

(defn parse-search-json [m]
  (let [{:keys [coordinates]} m]
    {:listing/geometry
     (some->> coordinates
              ((juxt :latitude :longitude))
              (map read-string)
              (apply st/point)
              honeysql.format/value)}))

(defn update-listing! [row]
  (-> {:update :listings
       :set (-> row
                :yad2-listing/search-json
                clojure.walk/keywordize-keys
                parse-search-json)
       :where [:= :yad2-listing/id (:yad2-listing/id row)]}
      db/sql
      db/execute!))
