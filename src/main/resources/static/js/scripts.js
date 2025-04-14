let page = 0;
let loading = false;
let lastPage = false;
let lastMonth = "";

const dateIndex = document.createElement("div");
dateIndex.id = "dateIndex";
document.body.appendChild(dateIndex);
document.addEventListener('DOMContentLoaded', aplicarMovimientoImagen);

// Para guardar referencias a los grupos mensuales y su selección
const monthGroups = {};

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

	const borrarForm = document.getElementById('borrarForm');
    borrarForm.addEventListener('submit', function(event) {
		event.preventDefault();
        borrarImagenes();                                    // Llamar a la función cargarImagenes()
    	cargarImagenes(true);
		window.addEventListener("scroll", detectarScroll);
    	aplicarZoom();
    });

	const seleccionarForm = document.getElementById('seleccionarForm');
    seleccionarForm.addEventListener('submit', function(event) {
		event.preventDefault();
        seleccionarImagenes();                                    // Llamar a la función cargarImagenes()
    });

    cargarImagenes();
    window.addEventListener("scroll", detectarScroll);
    aplicarZoom();
});

function cargarImagenes() {
    if (loading || lastPage) return; // Si ya no hay más imágenes, no seguir llamando

    loading = true;

    let searchTerm = document.getElementById("search") ? document.getElementById("search").value : " ";
    let searchTermDesde = document.getElementById("searchDateDesde") ? document.getElementById("searchDateDesde").value : " ";
    let searchTermHasta = document.getElementById("searchDateHasta") ? document.getElementById("searchDateHasta").value : " ";
	let busquedaParcial = document.getElementById("busquedaParcial").checked ? "parcial" : "total";

    let buscarDatos = "";
    if (searchTermDesde === "" && searchTermHasta === "") {
        buscarDatos = `/imagenes/miniaturas?page=${page}&size=25&query=${searchTerm}&busquedaParcial=${busquedaParcial}`;
    } else {
        buscarDatos = `/imagenes/miniaturas?page=${page}&size=25&query=${searchTerm}&searchTermDesde=${searchTermDesde}&searchTermHasta=${searchTermHasta}&busquedaParcial=${busquedaParcial}`;
    }

    fetch(buscarDatos)
        .then(response => response.json())
        .then(data => {
            let gallery = document.getElementById("gallery");
            let index = document.getElementById("dateIndex");

            data.content.forEach(img => {
                let imgMonth = new Date(img.fechaLateral).toLocaleString('es-ES', { month: 'long', year: 'numeric' });

                // Cabecera de grupo mensual
                if (imgMonth !== lastMonth) {
                    let header = document.createElement("div");
                    header.classList.add("month-header", "w-100", "d-flex", "align-items-center", "justify-content-between");

                    let headerText = document.createElement("span");
                    headerText.textContent = imgMonth;

                    let checkboxGroup = document.createElement("input");
                    checkboxGroup.type = "checkbox";
                    checkboxGroup.classList.add("checkbox-grupo");
                    checkboxGroup.dataset.mes = imgMonth;
                    checkboxGroup.title = "Seleccionar grupo";
                    checkboxGroup.onchange = (e) => {
                        document.querySelectorAll(`.checkbox-imagen[data-mes='${imgMonth}']`).forEach(cb => {
                            cb.checked = e.target.checked;
                        });
                    };

                    header.appendChild(headerText);
                    header.appendChild(checkboxGroup);
                    gallery.appendChild(header);

                    let indexItem = document.createElement("div");
                    indexItem.textContent = imgMonth;
                    indexItem.classList.add("index-item");
                    index.appendChild(indexItem);

                    lastMonth = imgMonth;
                }

                let colDiv = document.createElement("div");
                colDiv.classList.add("col-12", "col-sm-6", "col-md-4", "col-lg-3", "mb-3");

                let wrapper = document.createElement("div");
                wrapper.classList.add("position-relative");

                let imgElement = document.createElement("img");
                imgElement.src = img.urlMiniatura;
                imgElement.classList.add("thumbnail", "img-fluid");
                imgElement.onclick = () => abrirImagen(img.idOriginal);

                let checkbox = document.createElement("input");
                checkbox.type = "checkbox";
                checkbox.classList.add("checkbox-imagen");
                checkbox.dataset.id = img.idOriginal;
                checkbox.dataset.mes = imgMonth;
                checkbox.style.position = "absolute";
                checkbox.style.top = "5px";
                checkbox.style.left = "5px";
                checkbox.title = "Seleccionar imagen";

                wrapper.appendChild(imgElement);
                wrapper.appendChild(checkbox);
                colDiv.appendChild(wrapper);
                gallery.appendChild(colDiv);
            });

            if (data.last) {
                lastPage = true;
            } else {
                page++;
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

    if (!loading && !lastPage && scrollTop + windowHeight >= documentHeight - 200) {
        cargarImagenes();
    }
}

function abrirImagen(id) {
    const fullImage = document.getElementById("fullImage");
    fullImage.src = `/imagenes/original/${id}`;

    // Reiniciar el zoom y la traslación
    fullImage.style.transform = `translate(0px, 0px) scale(1)`;
    fullImage.style.cursor = "zoom-in";

    new bootstrap.Modal(document.getElementById("imageModal")).show();
}

function aplicarZoom() {
    let fullImage = document.getElementById("fullImage");
    let scale = 1;
    const zoomFactor = 0.1; // Ajusta la sensibilidad del zoom

    fullImage.addEventListener("wheel", function (event) {
        event.preventDefault(); // Evita el scroll de la página

        if (event.deltaY < 0) {
            // Scroll hacia arriba (zoom in)
            scale += zoomFactor;
        } else {
            // Scroll hacia abajo (zoom out)
            scale -= zoomFactor;
            if (scale < 1) {
                scale = 1; // No permitir zoom menor que el tamaño original
            }
        }

        fullImage.style.transform = `scale(${scale})`;
    }, { passive: false }); // passive: false es necesario para preventDefault en wheel

    // Opcional: Mantener la funcionalidad de click para un zoom rápido
    let zoomedOnClick = false;
    const clickScale = 2; // Factor de zoom al hacer clic

    fullImage.addEventListener("click", function () {
        if (!zoomedOnClick) {
            fullImage.style.transform = `scale(${clickScale})`;
            fullImage.style.cursor = "zoom-out";
            scale = clickScale; // Actualiza la escala
        } else {
            fullImage.style.transform = "scale(1)";
            fullImage.style.cursor = "zoom-in";
            scale = 1; // Restablece la escala
        }
        zoomedOnClick = !zoomedOnClick;
    });
}

function borrarImagenes (){
    const seleccionadas = Array.from(document.querySelectorAll(".checkbox-imagen:checked"))
        .map(cb => cb.dataset.id);
    if (seleccionadas.length === 0) {
        alert("No hay imágenes seleccionadas.");
        return;
    }

    fetch("/imagenes/borrarImagenes", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ids: seleccionadas.map(Number)})
    })
    .then(response => {
        if (response.ok) {
            const modalAlerta = new bootstrap.Modal(document.getElementById('alerta'));
            const modalTitle = document.querySelector('#alerta .modal-title');
            const modalBody = document.querySelector('#alerta .modal-body');

            modalTitle.textContent = 'Imágenes borradas'; // Establece el título
            modalBody.textContent = 'Las imágenes seleccionadas han sido borradas con éxito.'; // Establece el cuerpo

            modalAlerta.show();

            const modalElement = document.getElementById('alerta');
            modalElement.addEventListener('hidden.bs.modal', () => {
                location.reload();
            });

        } else {
            const modalAlertaError = new bootstrap.Modal(document.getElementById('alerta'));
            const modalTitleError = document.querySelector('#alerta .modal-title');
            const modalBodyError = document.querySelector('#alerta .modal-body');

            modalTitleError.textContent = 'Error al borrar';
            modalBodyError.textContent = 'Hubo un problema al intentar borrar las imágenes. Por favor, inténtalo de nuevo más tarde.';

            modalAlertaError.show();
        }
    })
    .catch(error => {
        console.error("Error en la petición:", error);
        const modalAlertaError = new bootstrap.Modal(document.getElementById('alerta'));
        const modalTitleError = document.querySelector('#alerta .modal-title');
        const modalBodyError = document.querySelector('#alerta .modal-body');

        modalTitleError.textContent = 'Error inesperado';
        modalBodyError.textContent = 'Ocurrió un error inesperado durante la operación. Por favor, inténtalo de nuevo.';

        modalAlertaError.show();
    });
}

function seleccionarImagenes (){ 
        document.querySelectorAll(".checkbox-imagen").forEach(checkbox => {
            checkbox.checked = true;
        });
}