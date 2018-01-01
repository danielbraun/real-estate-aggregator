/*jslint browser:true */
(function() {
    var google = window.google,
        Framework7 = window.Framework7,
        Backbone = window.Backbone,
        gmapSelector = "[data-google-maps]";

    function addMarkerToMap(markerData, mapInstance) {
        markerData.map = mapInstance;
        var M = new google.maps.Marker(markerData),
            info = markerData["info-window"];
        if (info) {
            M.addListener("click", function() {
                (new google.maps.InfoWindow(info)).open(mapInstance, M);
            });
        }
        return M;
    }

    function initializeGoogleMaps(el) {
        var data = JSON.parse(el.dataset.googleMaps),
            map = new google.maps.Map(el, data),
            MarkerCollection = Backbone.Collection.extend({
                url: data.url,
                model: Backbone.Model.extend({
                    idAttribute: "key"
                })
            }),
            markers = new MarkerCollection();

        markers.on({
            remove: function(model) {
                model.mapMarker.setMap(null);
                model.mapMarker = null;
            },
            add: function(model) {
                model.mapMarker = addMarkerToMap(model.toJSON(), map);
            }
        });

        map.addListener("idle", function() {
            markers.fetch({
                data: {
                    geometry: this.getBounds().toJSON()
                }
            });
        });
        return map;
    }

    function resizeGoogleMap(el) {
        google.maps.event.trigger(el, 'resize');
    }

    document.addEventListener("tab:show", function(e) {
        e.target.querySelectorAll(gmapSelector).forEach(resizeGoogleMap);
    });

    document.addEventListener("page:init", function(e) {
        e.target.querySelectorAll(gmapSelector).forEach(initializeGoogleMaps);
    });

    var myApp = new Framework7();
    document.querySelectorAll(".view").forEach(function(el) {
        myApp.addView(el, {
            dynamicNavbar: true
        });
    });
}());
