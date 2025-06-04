# 🍃 BioClick 🍃

## Descripción 

Este programa es una red social donde los Admins y Managers de la tienda Bioclick publican los productos disponibles, y los Usuarios pueden valorarlos y agregarlos a favoritos.</br> A futuro se añadirá la posibilidad de comprar los productos y recibir notificaciones.

>[!TIP]
>A la hora de hacer el docker-compose, las variables de entorno deberían ser:
>
>**Para Windows:**
>
>```powershell
>$env:SECRET="dlklkjflksjdflksjdlfkfjdlksjflksdjfdlksjfljfldskjflksdj"; `
>$env:SG_API_KEY="tu-apikey-de-Sendgrid"; `
>$env:DATASOURCE_URL="jdbc:postgresql://bioclick-sql/bioclick"; `
>$env:DATASOURCE_NAME="Bioclick"; `
>$env:DATASOURCE_PASSWORD="12345678"; `
>docker-compose up --build
>```
>
>**Para Linux:**
>
>```bash
>SECRET="dlklkjflksjdflksjdlfkfjdlksjflksdjfdlksjfljfldskjflksdj" \
>SG_API_KEY="tu-apikey-de-Sendgrid" \
>DATASOURCE_URL="jdbc:postgresql://bioclick-sql/bioclick" \
>DATASOURCE_NAME="Bioclick" \
>DATASOURCE_PASSWORD="12345678" \
>docker-compose up --build
>```



## Credenciales de acceso 🔑

A la hora de usar la base de datos en Postgres:

>[!IMPORTANT]
>Nombre del servidor: Bioclick (o el que quieras)</br>
>Dirección del servidor: bioclick-sql</br>
>Puerto: 5432</br>
>Nombre de usuario: Bioclick</br>
>Contraseña: 12345678

## Manual de Uso 📋
### Recursos necesarios
- Docker.
  
### Como usar
  ## Despues de haber usado docker-compose up --build
-Para ver la documentación de Swagger entrar en esta url: http://localhost:8080/swagger-ui/index.html#/</br>
-Para ver la base de datos de Postgresql usar esta url: http://localhost:5050
>[!IMPORTANT]
>Correo: admin@admin.com</br></br>
>Contraseña: 1

## Prueba de Peticiones

>[!NOTE]
>En las peticiones que buscan todas las entidades, se usa True para las que estan borradas y False para las que no.</br>
>Las variables están escritas en la colección de postman proporcionada (situada en la carpeta raiz del proyecto), por si se quiere cambiar alguna.
