(ns real-estate-aggregator.db
  (:require [clojure.java.jdbc :as jdbc]
            honeysql.core
            honeysql-postgres.format))

(def conn {:dbtype "postgresql"
           :dbname "real_estate_aggregator"})

(def execute! (comp (partial jdbc/execute! conn) honeysql.core/format))
(def insert! (partial jdbc/insert! conn))
(def insert-multi! (partial jdbc/insert-multi! conn))
(def query (comp (partial jdbc/query conn) honeysql.core/format))
