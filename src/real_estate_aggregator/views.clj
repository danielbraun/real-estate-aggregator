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
     "/app.js")]))

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

(defn listing-card [{:keys [title_1 title_2 line_1 line_2
                            img_url price]}]
  [:div.card
   [:div.card-header
    {:style {:background-image (format "url(%s)" img_url)
             :padding-bottom "50%"
             :background-size "cover"
             :background-position "center"}}]
   [:div.card-content
    [:div.card-content-inner
     (interpose [:br] [price title_1 title_2 line_1 line_2])]]])

(defn feed-page [{:keys [listings]}]
  [:div.page
   [:div.page-content
    (map listing-card listings)]])

(defn main-view [{{:keys [tab]} :params
                  :as context}]
  (base-layout
   [:div.view
    [:div.navbar
     [:div.navbar-inner
      [:div.center.sliding "חיפוש דירות"]]]
    [:div.pages.navbar-through.toolbar-through
     (case tab
       "search" (search-page)
       "feed" (feed-page context)
       [:div.page])]
    (f7/toolbar
     {:class "tabbar tabbar-labels"}
     (for [[id icon label]
           [["feed" :favorites "עדכונים"]
            ["search" :search "חיפוש"]
            ["saved" :heart "שמורים"]
            ["alerts" :bell "התראות"]]]
       [:a.tab-link {:href (str "?tab=" id)
                     :class (when (= tab id) "active")}
        (f7/icon {:f7 icon})
        (f7/tabbar-label label)]))]))
