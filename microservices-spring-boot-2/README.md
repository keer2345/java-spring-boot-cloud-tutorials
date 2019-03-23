1. [Implementing a Microservice Architecture with Spring Boot: Intro](https://medium.com/@marcus.eisele/implementing-a-microservice-architecture-with-spring-boot-intro-cdb6ad16806c)
1. [Microservices with Mo — Part One: Setting up docker](https://medium.com/@marcus.eisele/implementing-a-microservice-architecture-with-spring-boot-part-one-the-environment-cbc032473ab8)


At the `microservices-spring-boot-2` project's root path:
```
mvn clean
mvn package

docker-compose up -d --build
docker-compose up -d
docker-compose down -v
```
