# shortenerUrl

## Technologies
- MongoDB 4

Used for this project because it's just a collection with a count of redirect, original url and the alias, so don't need complex relationship between the objects and with mongoDB I can scale up and down the database.

- Docker

Used for create application image and run on container for be able to run many instancies in one machine, and for replicate the same enviroment between the instancies. For local is used to bring up a mongodb instancie for be able to run the code in a similar way of the production.

- Spring-Boot 2.2.5

Used Spring because of the abstracions of the framework and the easy and faster to write codes, for example with spring data is really fast to create a conection between the application and database.

- Java 11

Used Java version 11 because of the improved features to run JAVA in a instance inside a container.  

- Caffeine

Used Caffeine for caching the alias to original url because the original url from an alias never changes I can cache this data to be more fast and don't rely on a conection to the database to do this work. Caffeine is easier to setup cache because it's used the anotations of spring-cache and in addition to that with caffeine is possible to use ttl for cache. 

## How to run
Local: 
  - from the root of the project cd deps.
  - docker compose up -d for startup a mongo in a container.
  
  Cloud:
  
  https://beshortenerurl.herokuapp.com/shorteners

## API Documantion endpoints
Local: 
  http://localhost:8080/swagger-ui.html
 
 Cloud:
  https://beshortenerurl.herokuapp.com/swagger-ui.html
