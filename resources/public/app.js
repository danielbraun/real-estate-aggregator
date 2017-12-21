/*jslint browser:true */
(function() {
    var google = window.google;

    var initializeGoogleMaps = function(el) {
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
    };

    document.querySelectorAll("[data-google-maps]").forEach(initializeGoogleMaps);
}());
