(ns real-estate-aggregator.db
  (:require [clojure.java.jdbc :as jdbc]
            [clj-postgresql.core :as pg]
            [camel-snake-kebab.core :as csk]
            honeysql.core
            honeysql-postgres.format))

(def conn {:dbtype "postgresql"
           :dbname "real_estate_aggregator"})

(def sql #(honeysql.core/format % :quoting :ansi))
(def execute! (partial jdbc/execute! conn))
(def insert! (partial jdbc/insert! conn))
(def insert-multi! (partial jdbc/insert-multi! conn))
(def query
  (let [opts {:identifiers #(csk/->kebab-case % :separator \_)}]
    #(jdbc/query conn % opts)))
