package com.mrs.pet_project.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApi {

  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
            .info(new Info()
                    .title("Medical Records API")
                    .description("API for managing patients, doctors and requests")
                    .version("1.0.0"));
  }
}
