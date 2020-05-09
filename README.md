# Read Me
This is a Spring Boot app using Spring Security and JWT for authentication and authorization.
* Created an authentication REST endpoint
* Set up custom JWT Filter and Security Configurer to examine every incoming request, look for a valid JWT in header, parse JWT, verify JWT, and if valid, authorize the User so they can see the requested resource.
* This app uses an external JWT library (jjwt) for building JWTs and other JWT util functionality 

# Endpoints
* "/authenticate" - Accepts username and password in request body and returns JWT.
* "/hello" - shows the "resource" once user is authorized. In this case, "resource" is just a string.

# Getting Started

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/gradle-plugin/reference/html/)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Security](https://docs.spring.io/spring-boot/docs/2.2.6.RELEASE/reference/htmlsingle/#boot-features-security)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Securing a Web Application](https://spring.io/guides/gs/securing-web/)
* [Spring Boot and OAuth2](https://spring.io/guides/tutorials/spring-boot-oauth2/)
* [Authenticating a User with LDAP](https://spring.io/guides/gs/authenticating-ldap/)
