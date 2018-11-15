package org.satran.blockchain.graphql.rest;

import java.util.Collections;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("org.satran.blockchain.graphql.rest.controllers"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo());
  }

  private ApiInfo apiInfo() {
    return new ApiInfo(
        "REST API",
        "A REST way to explore",
        "API v1",
        null,
        new Contact("", "https://aion-graphql.com", "contact@aion-graphql.com"),
        "GPL-3.0", "https://github.com/satran004/aion-graphql/blob/master/LICENSE", Collections.emptyList());
  }
}
