/*jslint browser:true */
(function() {
    var google = window.google,
        Framework7 = window.Framework7,
        $ = window.$,
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
            markers = [];

        map.addListener("idle", function() {
            $.get(data.url, {
                geometry: this.getBounds().toJSON()
            }).
            then(function(response) {
                markers.forEach(function(marker) {
                    marker.setMap(null);
                });

                markers = response.map(function(marker) {
                    return addMarkerToMap(marker, map);
                });
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
