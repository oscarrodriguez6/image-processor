🔐 Autenticación y Registro
1. POST /usuarios/registro
Entrada: x-www-form-urlencoded

nombre: String

email: String

password: String

Salida:

200 OK: "Usuario registrado con éxito"

400 Bad Request: Mensaje de error (texto plano)

2. POST /usuarios/login
Entrada: x-www-form-urlencoded

email: String

password: String

Salida:

200 OK: JSON con token

json
Copiar
Editar
{ "token": "..." }
401 Unauthorized: Mensaje de error (texto plano)

⬆️ Carga de Imágenes
3. POST /imagenes/carga-masiva-postman
Entrada: multipart/form-data

Archivos

Otros parámetros como fecha, etiquetas, coordenadas (según implementación)

Salida:

200 OK: "Carga masiva completada correctamente"

400 Bad Request: Mensaje de error

4. POST /imagenes/carga-masiva-web
Entrada: multipart/form-data

Similar a /carga-masiva-postman

Salida:

Igual que arriba

📄 Vistas Web
5. GET /login
Salida: Devuelve la vista de login

6. GET /register
Salida: Devuelve la vista de registro

7. GET /home
Salida: Devuelve la vista principal con miniaturas y filtros

8. GET /cargar
Salida: Devuelve la vista para cargar imágenes desde web

9. GET /verMapa
Salida: Devuelve la vista del mapa con clusters o imágenes

🖼️ Gestión de Imágenes
10. GET /imagenes/original/{id}
Parámetros en URL: id: Long

Salida:

200 OK: Imagen original (image/jpeg o similar)

404 Not Found: Si no se encuentra

11. GET /miniaturas
Parámetros opcionales: fechaInicio, fechaFin, etiqueta, etc.

Salida:

200 OK: JSON con lista de imágenes con miniatura, metadatos, coordenadas

12. POST /imagenes/borrarImagenes
Entrada: application/json

json
Copiar
Editar
[1, 2, 3]  // IDs de imágenes
Salida:

200 OK: "Imágenes borradas correctamente"

500 Internal Server Error: Mensaje de error si algo falla

13. GET /coordenadas2
Parámetros:

minLat, minLng, maxLat, maxLng: Coordenadas del bounding box

zoom: Zoom actual del mapa

Salida:

200 OK: JSON con clusters o imágenes individuales según el zoom y cantidad

