# 建立项目
在 [start.spring.io](https://start.spring.io/) 构建项目，选择三个依赖：*Web, JPA, h2* 。

## 配置端口
默认端口为 `8080`，可以在文件 `application.properties` 中添加配置来修改：
```
server.port=8081
```

# Simple MVC Views
```xml
<dependency> 
    <groupId>org.springframework.boot</groupId> 
    <artifactId>spring-boot-starter-thymeleaf</artifactId> 
</dependency>
```
## Controller
```java
@Controller
public class SimpleController {
    @Value("${spring.application.name}")
    String appName;
 
    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        return "home";
    }
}
```

## Pages
home.html:
```html
<html>
<head><title>Home Page</title></head>
<body>
<h1>Hello !</h1>
<p>Welcome to <span th:text="${appName}">Our App</span></p>
</body>
</html>
```

# Security
```xml
<dependency> 
    <groupId>org.springframework.boot</groupId> 
    <artifactId>spring-boot-starter-security</artifactId> 
</dependency>
```
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .anyRequest()
            .permitAll()
            .and().csrf().disable();
    }
}
```

# Simple Persistence
- Book.java
- BookRepository.java
- Application.java, in this file add `@EnableJpaRepositories("com.keer.springbootstrap.persistence.repo")` and `@EntityScan("com.keer.springbootstrap.persistence.model")`

# Web and the Controller
- BookController.java
## Exception
- BookNotFoundException.java
- BookIdMismatchException.java

# Error Handling

# Testing
- org.apache.commons.text.RandomStringGenerator
- io.restassured.RestAssured
