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

document.getElementById("CargarForm").addEventListener("submit", function(event) {
    event.preventDefault();
    cargarArchivos();
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
    buscarDatos = " ";
    if (searchTermDesde == "" && searchTermHasta == "") { buscarDatos = `/imagenes/miniaturas?page=${page}&size=25&query=${searchTerm}`;}
    else {buscarDatos = `/imagenes/miniaturas?page=${page}&size=25&query=${searchTerm}&searchTermDesde=${searchTermDesde}&searchTermHasta=${searchTermHasta}`;}
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
			    imgElement.src = img.urlMiniatura + "?v=" + new Date().getTime(); // Evita caché;
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

async function cargarArchivos() {
    const formData = new FormData();
    const carpetaFiles = document.getElementById("cargarCarpeta").files;
    const individualFiles = document.getElementById("CargarImagenes").files;

    // Separar imágenes y vídeos
    const imagenes = [];
    const videos = [];

    for (let i = 0; i < carpetaFiles.length; i++) {
        const file = carpetaFiles[i];
        if (file.type.startsWith("image/")) {
            imagenes.push(file);
        } else if (file.type.startsWith("video/")) {
            videos.push(file);
        }
    }

    for (let i = 0; i < individualFiles.length; i++) {
        imagenes.push(individualFiles[i]);
    }

    // Añadir imágenes a formData
    imagenes.forEach(img => formData.append("files", img, img.webkitRelativePath || img.name));

    // Procesar y añadir vídeos comprimidos
    for (let i = 0; i < videos.length; i++) {
        const compressedVideo = await procesarVideo(videos[i]);
        if (compressedVideo) {
            formData.append("files", compressedVideo, videos[i].name);
        }
    }

    mostrarLoadingOverlay();

    fetch('/imagenes/carga-masiva', { 
        method: 'POST',
        body: formData
					 
																		
			
    })
    .then(response => {
        if (!response.ok) {
            return response.json().then(error => { throw new Error(error.error); });
											 
			   
        }
        return response.json();
    })
    .then(data => {
        console.log(data);
        ocultarLoadingOverlay();
        mostrarMensajeExito("Archivos cargados con éxito.");
    })
    .catch(error => {
        console.error('Error:', error);
        ocultarLoadingOverlay();
        mostrarMensajeError(error.message);
    });
}

function mostrarLoadingOverlay() {
    document.getElementById("loading-overlay").style.display = "block";
									
}

function ocultarLoadingOverlay() {
    document.getElementById("loading-overlay").style.display = "none";
								   
}

function mostrarMensajeError(mensaje) {
    const errorDiv = document.getElementById("error-message");
    errorDiv.textContent = mensaje;
    errorDiv.style.display = "block";
}

function mostrarMensajeExito(mensaje) {
    const successDiv = document.getElementById("success-message");
    successDiv.textContent = mensaje;
    successDiv.style.display = "block";
}

// Procesar video con FFmpeg.js
async function procesarVideo(videoFile) {

	console.time("Tiempo de compresión"); // Inicia el temporizador
    console.log("Procesando video:", videoFile.name);

    // Cargar FFmpeg.js
    const { createFFmpeg, fetchFile } = FFmpeg;
    const ffmpeg = createFFmpeg({ log: true });

    if (!ffmpeg.isLoaded()) {
        await ffmpeg.load();
    }

    // Cargar el archivo de video en FFmpeg
    await ffmpeg.FS('writeFile', 'input.mp4', await fetchFile(videoFile));

    // Comprimir con H.264 y reducir resolución a 720p si es necesario
    await ffmpeg.run('-i', 'input.mp4', '-vf', 'scale=-1:720', '-b:v', '1500k', '-preset', 'fast', '-c:a', 'aac', '-b:a', '128k', '-map_metadata', '0', 'output.mp4');

    // Obtener el archivo comprimido
    const data = ffmpeg.FS('readFile', 'output.mp4');

    console.timeEnd("Tiempo de compresión"); // Finaliza y muestra el tiempo en consola
    // Convertirlo a un Blob y devolverlo
    return new File([data.buffer], videoFile.name, { type: 'video/mp4' });
}

