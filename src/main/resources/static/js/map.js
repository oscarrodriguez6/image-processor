document.addEventListener("DOMContentLoaded", function () {
    // Crear el mapa centrado en la ubicación inicial
    const map = L.map('map').setView([40.5916, -4.1477], 15);

    // Definir capas de mapa: Normal (OSM) y Satélite (Esri)
    const osmLayer = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; OpenStreetMap contributors',
        crossOrigin: true
    });

    const esriSatLayer = L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
        attribution: '&copy; Esri &mdash; Source: Esri, Maxar, Earthstar Geographics',
        crossOrigin: true
    });

    // Agregar la capa OSM por defecto
    esriSatLayer.addTo(map);

    // Grupo de marcadores con clustering
    let markers = L.markerClusterGroup();
    map.addLayer(markers);

    // Función para cargar imágenes dentro del área visible del mapa
    function cargarImagenes(bounds) {
        fetch(`/coordenadas2?minLat=${bounds.getSouth()}&maxLat=${bounds.getNorth()}&minLon=${bounds.getWest()}&maxLon=${bounds.getEast()}`)
            .then(response => response.json())
            .then(data => {
                markers.clearLayers(); // Limpiar marcadores previos

                data.forEach(item => {
                    if (item.hasOwnProperty('cantidad')) {
                        // Es un cluster
                        let clusterMarker = L.marker([item.latitud, item.longitud]);
                        clusterMarker.bindPopup(`<b>${item.cantidad} imágenes en esta área</b>`);
                        markers.addLayer(clusterMarker);
                    } else {
                        // Es una imagen individual
                        let marker = L.marker([item.latitud, item.longitud]).addTo(map);
                        marker.bindPopup(`<div class='map-popup'><img src='${item.urlMiniatura}' onclick='abrirImagen(${item.id})'></div>`);
                        markers.addLayer(marker);
                    }
                });
            })
            .catch(error => console.error("Error cargando imágenes:", error));
    }

    // Cargar imágenes al mover el mapa
    map.on('moveend', function () {
        cargarImagenes(map.getBounds());
    });

    // Cargar imágenes al inicio
    cargarImagenes(map.getBounds());

    // Función para abrir imagen en el modal
    window.abrirImagen = function (id) {
        document.getElementById("fullImage").src = `/imagenes/original/${id}`;
        new bootstrap.Modal(document.getElementById("imageModal")).show();
    };

    // Alternar entre capas de mapa
    let usandoSat = false;
    document.getElementById("toggleLayer").addEventListener("click", function () {
        if (usandoSat) {
            map.removeLayer(esriSatLayer);
            osmLayer.addTo(map);
            this.textContent = "Cambiar a Satélite";
        } else {
            map.removeLayer(osmLayer);
            esriSatLayer.addTo(map);
            this.textContent = "Cambiar a Mapa Normal";
        }
        usandoSat = !usandoSat;
    });
});
