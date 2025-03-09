package com.ComNCheck.ComNCheck.domain.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    SecurityScheme securityScheme = new SecurityScheme()
            .type(Type.APIKEY)
            .in(In.COOKIE).name("AccessToken");
    SecurityRequirement securityRequirement = new SecurityRequirement().addList("cookieAuth");
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("cookieAuth", securityScheme))
                .addSecurityItem(securityRequirement)
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("ComNCheck Swagger")
                .description("ComNCheck API")
                .version("1.0.0");
    }
}
