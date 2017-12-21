/*jslint browser:true */

(function() {
    var google = window.google;
    document.querySelectorAll("[data-google-maps]").forEach(function(el) {
        return new google.maps.Map(el, JSON.parse(el.dataset.googleMaps));
    });
}

());
