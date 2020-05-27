package de.openvalidation.openvalidationidebackend;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OpenvalidationIdeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenvalidationIdeBackendApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI(@Value("${springdoc.version}") String appVersion) {
		return new OpenAPI()
				.components(new Components().addSecuritySchemes("basicScheme",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic")))
				.info(new Info()
						.title("openVALIDATION IDE Backend")
						.version(appVersion)
						.description("Find the project on [GitHub](https://github.com/openvalidation/openvalidation-ide)"));
	}

}
