package com.backend.malhaedo.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class SwaggerConfig {

    @Bean
    public OpenAPI MalHaeDoAPI() {
        Info info = new Info()
                .title("말해도 API")
                .description("포텐데이 비사이드 말해도 API 명세서")
                .version("1.0.0");

        String jwtSchemeName = "JWT TOKEN";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .addServersItem(new Server().url("/"))
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
//        return new OpenAPI()
//                .addServersItem(new Server().url("/"))
//                .info(info)
//                .addSecurityItem(securityRequirement)
//                .components(components)
//                .addSecurityItem(new SecurityRequirement().addList(jwtSchemeName))
//                .path("/api/v0/prompt/reply", new io.swagger.v3.oas.models.PathItem()
//                        .post(new io.swagger.v3.oas.models.Operation()
//                                .addParametersItem(new io.swagger.v3.oas.models.parameters.Parameter()
//                                        .in("header")
//                                        .name("Authorization")
//                                        .description("Bearer token")
//                                        .required(true)
//                                        .schema(new io.swagger.v3.oas.models.media.StringSchema())
//                                )
//                        )
//                );
    }
}
