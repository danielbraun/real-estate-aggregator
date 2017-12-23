(ns real-estate-aggregator.views
  (:require [hiccup.page :as page]
            [hiccup.form :as form]
            [hiccup.core :refer [html]]
            cheshire.core
            [hiccup-framework7.components :as f7]
            [real-estate-aggregator.routes :refer [url-for]]))

(def json cheshire.core/generate-string)

(defmulti action-view :handler)
(defmulti action-layout :handler)

(defn base-layout [& content]
  (page/html5
   {}
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name :apple-mobile-web-app-capable :content :yes}]
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
     "https://cdnjs.cloudflare.com/ajax/libs/framework7/1.6.5/js/framework7.min.js"
     "/app.js")]))

(defn bg-image-style [{:keys [img_url]}]
  {:background-image (format "url(%s)" img_url)
   :padding-bottom "50%"
   :background-size "cover"
   :background-position "center"})

(defn listing-card [{:keys [title_1 title_2 line_1 line_2
                            img_url price]
                     :as context}]
  [:div.card
   [:div.card-header
    {:style (bg-image-style context)}]
   [:div.card-content
    [:div.card-content-inner
     (interpose [:br] [price title_1 title_2 line_1 line_2])]]
   [:div.card-footer
    [:a.link {:href (url-for :listings/show context)} "קרא עוד"]]])

(defn listing-marker [{:keys [coordinates price] :as listing}]
  {:position {:lat (read-string (:latitude coordinates))
              :lng (read-string (:longitude coordinates))}
   :label price
   :info-window {:content (html (listing-card listing))}})

(defn search-page [{:keys [listings]}]
  [:div.page
   (f7/search-bar {:placeholder "חיפוש" :cancel-link "ביטול"})
   [:div.searchbar-overlay]
   [:div.page-content
    [:div
     {:style "height: 100%"
      :data-google-maps
      (json
       {:zoom 15
        :markers (->> listings
                      (filter :coordinates)
                      (map listing-marker))
        :center {:lat 32.0767849 :lng 34.8048163}})}]]])

(defn feed-page [{:keys [listings]}]
  (f7/page (map listing-card listings)))

(defn main-view [{{:keys [tab]} :params
                  :as context}]
  (base-layout
   (f7/view
    (f7/navbar {:title "חיפוש דירות", :back-link nil})
    (f7/pages {:class "navbar-through"}
              (case tab
                "search" (search-page context)
                "feed" (feed-page context)
                (f7/page)))
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
        (f7/tabbar-label label)])))))

(defn listing-page [{:keys [images] :as ctx}]
  (list
   (f7/navbar {:title "Listing" :back-link "אחורה"})
   (f7/page
    (f7/swiper
     {:class "swiper-init"
      :data-swiper (json {:pagination ".swiper-pagination"})}
     (for [image images]
       (f7/swiper-slide {:style (bg-image-style {:img_url image})}))))))
