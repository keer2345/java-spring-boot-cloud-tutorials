**Microservices with Spring Boot — Creating our Microserivces & Gateway (Part 2)**

> https://medium.com/omarelgabrys-blog/microservices-with-spring-boot-creating-our-microserivces-gateway-part-2-31f8aa6b215b

![](https://raw.githubusercontent.com/keer2345/java-spring-boot-cloud-tutorials/master/microservices-spring-boot/images/01.png)

我们的应用包括注册服务、图像服务、画廊服务和网管。画廊服务基于图像服务，检索所有图像并显示。

> 我们的版本基于 spring-boot 2

# Eureka Server
这是服务的名称，或称为注册服务。它的责任是给微服务起名。为什么呢？
1. 微服务不必指定 IP 地址。
1. 如果服务需要动态 IP 地址，可以自动扩展。

每一个服务用 Eureka 注册，并 ping Eureka 服务来通知它还或者。如果 Eureka 没有从一个服务收到通知，则这个服务将从 Eureka 服务中自动的变成未注册。

相当简单的步骤，我们开始吧！

创建一个 Maven 项目，或者使用 [Spring Initializr](https://start.spring.io/)。确保包含了 Web, Eureka Server 和 DevTools（可选）依赖。

``` xml
<dependencies>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-web</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.cloud</groupId>
		<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-devtools</artifactId>
		<optional>true</optional>
	</dependency>
</dependencies>
```

接下来，配置 `application.properties`：

```
# Give a name to the eureka server
spring.application.name=eureka-server

# default port for eureka server
server.port=8761

# eureka by default will register itself as a client. So, we need to set it to false.
# What's a client server? See other microservices (image, gallery, auth, etc).
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

最后，在主引导类中声明 `@EnableEurekaServer` 来让 Eureka 服务可用。

``` java
package com.eureka.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer    // Enable eureka server
public class SpringEurekaServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringEurekaServerApplication.class, args);
	}

}
```

下一步创建我们的服务，图像和画廊。

# 图像服务

Eureka 客户端服务是微服务架构中独立的服务，它可用支付、账户、通知、认证、配置等等。

图像服务作为图像的数据来源，每个图像拥有 id, title 和 url，很简单吧？

相对 Eureka Server，我们使用 Eureka Client：

``` xml
<dependencies>
	 <dependency>
		<groupId>org.springframework.boot</groupId>
		 <artifactId>spring-boot-starter-web</artifactId>
	 </dependency>
	 <dependency>
		 <groupId>org.springframework.cloud</groupId>
		 <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
	 </dependency>
	 <dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-data-rest</artifactId>
	</dependency>
	 <dependency>
		 <groupId>org.springframework.boot</groupId>
		 <artifactId>spring-boot-devtools</artifactId>
		 <optional>true</optional>
	 </dependency>
</dependencies>
```

编辑配置文件 `application.properties`：

```
# serivce name
spring.application.name=image-service
# port
server.port=8200
# eureka server url
eureka.client.service-url.default-zone=http://localhost:8761/eureka
```

在主引导类添加声明 `@EnableEurekaClient`:

``` java
package com.eureka.image;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient    // Enable eureka client. It inherits from @EnableDiscoveryClient.
public class SpringEurekaImageApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringEurekaImageApplication.class, args);
	}

}
```

选择，我们的图像服务将要通过端口公开数据。因此，我们需要创建控制器，并定义 action 方法：

``` java
package com.eureka.image.controller;

import com.eureka.image.entities.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/")
public class HomeController {
	@Autowired
	private Environment env;

	@RequestMapping("/images")
	public List<Image> getImages() {
		List<Image> images = Arrays.asList(
				new Image(1, "Treehouse of Horror V", "https://www.imdb.com/title/tt0096697/mediaviewer/rm3842005760"),
				new Image(2, "The Town", "https://www.imdb.com/title/tt0096697/mediaviewer/rm3698134272"),
				new Image(3, "The Last Traction Hero", "https://www.imdb.com/title/tt0096697/mediaviewer/rm1445594112")
		);
		return images;
	}
}
```

# 画廊服务
画廊服务调用图像服务来获取一系列的图像，或者只是创建特定图像。从一个 REST 客户端调用其他服务可以使用：
1. RestTemplate，一个可以发送请求到 REST API 服务的对象。
1. FeignClient (角色类似代理)，提供类似 RestTemplate 的功能。

这两者都通过负载均衡来请求服务。

> **什么是负载均衡？**

> 如果服务有多个实例运行在不同的端口，我们需要将这些服务的实例均衡分配请求。当使用 `Ribbon` 方法（默认）时，请求将被平均分配。

我们创建一个和图像服务一样的 Maven 对象。并添加配置：

```
spring.application.name=gallery-service
server.port=8100
eureka.client.service-url.default-zone=http://localhost:8761/eureka
```

在主引导类中，除了激活 Eureka Client 外，我们还需要创建 `RestTemplate` 的 bean 来调用图像服务。

``` java
　package com.eureka.gallery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient  // Enable Eureka Client
public class SpringEurekaGalleryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringEurekaGalleryApplication.class, args);
	}

}

@Configuration
class RestTemplateConfig {

	// Create a bean for restTemplate to call services
	@Bean
	@LoadBalanced    // Load balance between service instances running at different ports.
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
```

在控制器中，使用 `RestTemplate` 来调用图像服务：
