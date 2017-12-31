(ns real-estate-aggregator.db
  (:require [clojure.java.jdbc :as jdbc]
            [clj-postgresql.core :as pg]
            [camel-snake-kebab.core :as csk]
            honeysql-postgres.format))

(def conn {:dbtype "postgresql"
           :dbname "real_estate_aggregator"
           :identifiers #(csk/->kebab-case % :separator \_)})

(def execute! (partial jdbc/execute! conn))
(def insert! (partial jdbc/insert! conn))
(def insert-multi! (partial jdbc/insert-multi! conn))
(def query (partial jdbc/query conn))
