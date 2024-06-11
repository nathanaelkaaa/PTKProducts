# Spring Boot + PostgreSQL Dockerized

### Creating a docker network
``` bash
docker network create ptk_products
``` 

### Spring Boot docker setup
``` bash
#Build Spring boot image (From the position of the Dockerfile)
docker build . -t ptk_products_service

#Run backend service and hos tit on localhost port 8082
docker run -d -p 8082:8080 --name ptk_products_backend --network ptk_products ptk_products_service

```

### Access To RabbitMq
``` 
http://localhost:15672/
user : guest
password : guest
```