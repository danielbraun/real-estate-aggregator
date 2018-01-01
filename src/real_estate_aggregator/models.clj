(ns real-estate-aggregator.models
  (:require [clj-http.client :as http]
            [real-estate-aggregator.db :as db]
            [honeysql.core :as sql]
            [yad2-api.mobile :as yad2]
            [clj-postgresql.spatial :as st]))

(defmulti retrieve :dataset)

(defmethod retrieve :listings [{:keys [geometry]}]
  (->> {:select [:*]
        :from [:listings]
        :limit 25
        :where [:ST_Intersects
                :geometry
                (let [{:keys [north south east west]
                       :or {east 100, west 0, north 100, south 0}} geometry]
                  (sql/call :ST_MakeEnvelope south west north east))]}
       sql/format
       db/query
       (map #(update % :search-json clojure.walk/keywordize-keys))))

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
  {:yad2/id id
   :search-json (pr-str ad)})

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
       :upsert {:on-conflict [:yad2/id]
                :do-update-set [:search-json]}}
      sql/format
      db/execute!))

(defn parse-search-json [m]
  (let [{:keys [coordinates]} m]
    {:geometry
     (some->> coordinates
              ((juxt :latitude :longitude))
              (map read-string)
              (apply st/point))}))

(defn update-listing! [row]
  (-> {:update :listings
       :set (-> row
                :search-json
                clojure.walk/keywordize-keys
                parse-search-json)
       :where [:= :yad2/id (:yad2/id row)]}
      sql/format
      db/execute!))
