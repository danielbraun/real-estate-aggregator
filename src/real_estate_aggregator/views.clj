(ns real-estate-aggregator.views
  (:require [hiccup.page :as page]
            [cheshire.core :as json]
            [hiccup-framework7.components :as f7]
            [real-estate-aggregator.routes :refer [url-for]]))

(defmulti action-view :handler)
(defmulti action-layout :handler)

(defn base-layout [& content]
  (page/html5
    {}
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name :viewport
             :content "width=device-width, initial-scale=1, maximum-scale=1, minimum-scale=1, user-scalable=no, minimal-ui"}]
     [:title "My App"]
     (page/include-css
       "https://cdnjs.cloudflare.com/ajax/libs/framework7/1.6.5/css/framework7.ios.min.css"
       "https://cdnjs.cloudflare.com/ajax/libs/framework7/1.6.5/css/framework7.ios.rtl.min.css"
       "https://cdnjs.cloudflare.com/ajax/libs/framework7/1.6.5/css/framework7.ios.colors.min.css"
       "https://cdn.framework7.io/css/framework7-icons.css")]
    [:body
     [:div.framework7-root
      [:div.statusbar-overlay]
      [:div.panel-overlay]
      [:div.panel.panel-left.panel-reveal
       [:div.content-block
        [:p "Left panel content goes here"]]]
      [:div.views content]]
     (page/include-js
       "https://maps.googleapis.com/maps/api/js?key=AIzaSyDdE61DCVlHTCWGiUNUgiq2Eg-_DrajfAg&language=he"
       "/app.js")
     ]))



(defn search-page []
  [:div.page
   [:form.searchbar
    [:div.searchbar-input
     [:input {:type :search :placeholder "חיפוש"}
      [:a.searchbar-clear {:href "#"}]]]
    [:a.searchbar-cancel {:href "#"}]]

   [:div.searchbar-overlay]
   [:div.page-content
    [:div
     {:style "height: 100%"
      :data-google-maps
      (json/generate-string
        {:zoom 15
         :center {:lat 32.0767849 :lng 34.8048163}})}]]])

(defn main-view [_]
  (base-layout
    [:div.view
     [:div.navbar
      [:div.navbar-inner
       [:div.center.sliding "חיפוש דירות"]]]
     [:div.pages.navbar-through.toolbar-through
      (search-page)
      ]
     (f7/toolbar
       {:class "tabbar tabbar-labels"}
       [:a.tab-link {:href (url-for :feed {})}
        (f7/icon {:f7 :favorites})
        (f7/tabbar-label "עדכונים")]
       [:a.tab-link.active {:href (url-for :search {})}
        (f7/icon {:f7 :search})
        (f7/tabbar-label "חיפוש")]
       [:a.tab-link {:href (url-for :saved {})}
        (f7/icon {:f7 :heart})
        (f7/tabbar-label "שמורים")]
       [:a.tab-link {:href (url-for :alerts {})}
        (f7/icon {:f7 :bell})
        (f7/tabbar-label "התראות")])]))
