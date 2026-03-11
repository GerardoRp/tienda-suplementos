El archivo src/main/resources/application.properties está configurado con el usuario root. Por favor, actualiza la contraseña en ese archivo (spring.datasource.password) con tu contraseña local de MySQL antes de ejecutar el proyecto.

Ejecución del Proyecto
El proyecto está configurado para ejecutarse en el puerto 8081 para evitar conflictos.

Una vez iniciada la aplicación, los servicios REST pueden ser probados en las siguientes rutas (ejemplo con Categorías):

GET http://localhost:8081/api/categorias

POST http://localhost:8081/api/categorias