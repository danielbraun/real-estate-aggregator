(defproject real-estate-aggregator "0.1.0-SNAPSHOT"
  :ring {:handler real-estate-aggregator.core/handler
         :auto-refresh? true
         :nrepl {:start? true}}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring/ring-defaults "0.3.1"]
                 [bidi-rest "0.1.0"]
                 [bidi "2.1.2"]
                 [metosin/ring-http-response "0.9.0"]
                 [ring-middleware-format "0.7.2"]
                 [ring-middleware-accept "2.0.3"]
                 [clj-http "3.7.0"]
                 [hiccup "2.0.0-alpha1"]
                 [yad2-api "0.1.0-SNAPSHOT"]])
