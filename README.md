# Spring Boot Issue

This reproduces an [issue](https://github.com/spring-projects/spring-boot/issues/36657) where using Graal and tests with a random server port in a kotlin spring boot app fails compliation during test execution.

This also includes running a management server on another port to cause the issue

To reproduce:

    `./gradlew test`

Possible changes to prevent failure:
 - Remove the `management.server.port` property from application.properties
  - Remove the `SpringBootTest.WebEnvironment.RANDOM_PORT` webEnvironment from the SpringBootTest annotation
  - Remove the `org.graalvm.buildtools.native` plugin from the build.