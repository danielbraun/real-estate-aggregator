/*jslint browser:true */
(function() {
    var google = window.google,
        Framework7 = window.Framework7,
        gmapSelector = "[data-google-maps]";

    function initializeGoogleMaps(el) {
        var data = JSON.parse(el.dataset.googleMaps),
            map = new google.maps.Map(el, data);
        (data.markers || []).forEach(function(marker) {
            marker.map = map;
            var M = new google.maps.Marker(marker),
                info = marker["info-window"];
            if (info) {
                M.addListener("click", function() {
                    (new google.maps.InfoWindow(info)).open(map, M);
                });
            }
            return M;
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
