let page = 0;
let loading = false;
let lastPage = false; // Nueva variable para saber si ya no hay más imágenes

let lastMonth = "";

const dateIndex = document.createElement("div");
dateIndex.id = "dateIndex";
document.body.appendChild(dateIndex);

document.addEventListener("DOMContentLoaded", function () {
	const searchForm = document.getElementById('searchForm');

    searchForm.addEventListener('submit', function(event) {
        event.preventDefault();                              // Prevenir el comportamiento por defecto del formulario
        page = 0;                                            // Reiniciar la página
        lastPage = false;                                    // Reiniciar el estado de la última página
        lastMonth = "";
        document.getElementById('gallery').innerHTML = '';   // Limpiar las imágenes anteriores
        document.getElementById('dateIndex').innerHTML = '';
        cargarImagenes();                                    // Llamar a la función cargarImagenes()
    });
	aplicarZoom();
    cargarImagenes();
    window.addEventListener("scroll", detectarScroll);
});

function aplicarZoom() {
	let fullImage = document.getElementById("fullImage");
	let zoomed = false;

	fullImage.addEventListener("click", function () {
	    if (!zoomed) {
	        fullImage.style.transform = "scale(2)"; // Doble de tamaño
	        fullImage.style.cursor = "zoom-out";
	    } else {
	        fullImage.style.transform = "scale(1)"; // Vuelve al tamaño original
	        fullImage.style.cursor = "zoom-in";
	    }
	    zoomed = !zoomed;
	});

}

function cargarImagenes() {
    if (loading || lastPage) return; // Si ya no hay más imágenes, no seguir llamando

    loading = true;

    let searchTerm = document.getElementById("search") ? document.getElementById("search").value : " ";
    let searchTermDesde = document.getElementById("searchDateDesde") ? document.getElementById("searchDateDesde").value : " ";
    let searchTermHasta = document.getElementById("searchDateHasta") ? document.getElementById("searchDateHasta").value : " ";
	let busquedaParcial = document.getElementById("busquedaParcial").checked ? "parcial" : "total";
    buscarDatos = " ";
    if (searchTermDesde == "" && searchTermHasta == "") { buscarDatos = `/imagenes/miniaturas?page=${page}&size=25&query=${searchTerm}&busquedaParcial=${busquedaParcial}`;}
    else {buscarDatos = `/imagenes/miniaturas?page=${page}&size=25&query=${searchTerm}&searchTermDesde=${searchTermDesde}&searchTermHasta=${searchTermHasta}$busquedaParcial=${busquedaParcial}`;}
    fetch(buscarDatos)
        .then(response => response.json())
        .then(data => {
            let gallery = document.getElementById("gallery");
			let index = document.getElementById("dateIndex");

			 data.content.forEach(img => {
			    let imgMonth = new Date(img.fechaLateral).toLocaleString('es-ES', { month: 'long', year: 'numeric' });
			    
			    if (imgMonth !== lastMonth) {
			        let header = document.createElement("div");
			        header.textContent = imgMonth;
			        header.classList.add("month-header", "w-100");
			        gallery.appendChild(header);
			        
			        let indexItem = document.createElement("div");
			        indexItem.textContent = imgMonth;
			        indexItem.classList.add("index-item");
			        index.appendChild(indexItem);
			        
			        lastMonth = imgMonth;
			    }
			    
			    let colDiv = document.createElement("div");
			    colDiv.classList.add("col-12", "col-sm-6", "col-md-4", "col-lg-3", "mb-3");
			
			    // Determinar si la imagen es horizontal o vertical y aplicar la clase correspondiente
			    let imgElement = document.createElement("img");
			    imgElement.src = img.urlMiniatura;
			    imgElement.classList.add("thumbnail", "img-fluid");
			
			    if (img.ancho > img.alto) { // Ajustar la lógica según las propiedades de tu objeto img
			        colDiv.classList.add("horizontal");
			    }
			
			    imgElement.onclick = () => abrirImagen(img.idOriginal);
			    
			    colDiv.appendChild(imgElement);
			    gallery.appendChild(colDiv);
			});

            if (data.last) {
                lastPage = true; // Marcar que ya no hay más páginas
            } else {
                page++; // Incrementar solo si hay más imágenes
            }

            loading = false;
        })
        .catch(error => {
            console.error("Error cargando imágenes:", error);
            loading = false;
        });
}
function detectarScroll() {
    let scrollTop = window.scrollY;
    let windowHeight = window.innerHeight;
    let documentHeight = document.documentElement.scrollHeight;

    // Si estamos cerca del final de la página y hay más imágenes, cargamos más
    if (!loading && !lastPage && scrollTop + windowHeight >= documentHeight - 200) {
        cargarImagenes();
    }
}

function abrirImagen(id) {
    document.getElementById("fullImage").src = `/imagenes/original/${id}`;
    new bootstrap.Modal(document.getElementById("imageModal")).show();
}

document.addEventListener("DOMContentLoaded", function () {
    let cerrarModal = document.createElement("button");
    cerrarModal.innerText = "X";
    cerrarModal.classList.add("btn", "btn-danger", "cerrar-modal");
    cerrarModal.onclick = function () {
        let modal = bootstrap.Modal.getInstance(document.getElementById("imageModal"));
        modal.hide();
    };

    let modalBody = document.querySelector("#imageModal .modal-body");
    modalBody.appendChild(cerrarModal);
});
