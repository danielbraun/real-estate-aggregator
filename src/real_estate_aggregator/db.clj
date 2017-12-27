(ns real-estate-aggregator.db
  (:require [clojure.java.jdbc :as jdbc]
            [camel-snake-kebab.core :as csk]
            honeysql.core
            honeysql-postgres.format))

(def conn {:dbtype "postgresql"
           :dbname "real_estate_aggregator"})

(def execute!
  (comp (partial jdbc/execute! conn)
        #(honeysql.core/format % :quoting :ansi)))
(def insert! (partial jdbc/insert! conn))
(def insert-multi! (partial jdbc/insert-multi! conn))
(def query
  (let [opts {:identifiers #(csk/->kebab-case % :separator \_)}]
    (comp #(jdbc/query conn % opts) honeysql.core/format)))
