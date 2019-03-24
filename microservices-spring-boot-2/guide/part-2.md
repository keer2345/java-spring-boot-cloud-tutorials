> https://medium.com/@marcus.eisele/implementing-a-microservice-architecture-with-spring-boot-part-one-the-environment-cbc032473ab8

# 使用Spring Boot实现微服务架构（2）——设置Docker

第一篇文章概述地介绍了微服务以及我们将要完成的项目。这篇文章的目标是构建 Docker，为我们的微服务架构添加容器。

## 什么是Docker
关于Docker的知识相比您或多或少已经有些了解，它的核心便是容器，各容器间是相互独立的。它的好处是隔离，自动创建和在不同环境间的可移植性。

我们可以参考 Spring.io 官网上的 Docker 指南来设置 Docker。这里我们用到的是 Dockerfile + docker-compose.yml 。代码分别如下：

```
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/template-service.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
```

```
version: '3'

services:
  counterservice:
    build: template-service
    ports:
      - 8080:8080
```

上面知识示例代码，我们在接下来的模块中会具体配置这些代码。下一篇文章我们将编写一个具体的项目，并通过 Docker 运行起来。
