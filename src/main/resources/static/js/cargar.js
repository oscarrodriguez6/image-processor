document.getElementById("CargarForm").addEventListener("submit", function(event) {
    event.preventDefault();
    cargarArchivos();
});

async function cargarArchivos() {
    const formData = new FormData();
    const carpetaFiles = document.getElementById("cargarCarpeta").files;
    const individualFiles = document.getElementById("CargarImagenes").files;

    // Filtrar solo imágenes
    const imagenes = [];

    for (let i = 0; i < carpetaFiles.length; i++) {
        const file = carpetaFiles[i];
        if (file.type.startsWith("image/")) {
            imagenes.push(file);
        }
    }

    for (let i = 0; i < individualFiles.length; i++) {
        const file = individualFiles[i];
        if (file.type.startsWith("image/")) {
            imagenes.push(file);
        }
    }

    // Añadir imágenes a formData
    imagenes.forEach(img => formData.append("files", img, img.webkitRelativePath || img.name));

    mostrarLoadingOverlay();

    fetch('/imagenes/carga-masiva-web', { 
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