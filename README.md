# Spring boot application with Authentication jwt and docker

## Endpoints

  - Login

  - Register

### Dockerizing application

  - Dockerfile
    
    Stage n°1, generara un jar que contendrá toda la aplicación spring boot.
    
    Stage n°2, Copia el JAR generado en la primera etapa, lo renombra con el nombre app.jar y luego ejecuta ese jar que iniciara la aplicación Spring Boot.
    
  - docker-compose.yml

    Levanta 2 servicios, uno que creara un contenedor de una imagen de Mysql , y otra que levantara un contenedor de la imagen que se creo en el Dockerfile
