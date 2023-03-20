package com.m1a2st.simplebackendpractice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import org.springframework.context.annotation.Configuration;

/**
 * @Author m1a2st
 * @Date 2023/3/20
 * @Version v1.0
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "OpenAPI 3 Practice",
                description = "springdoc-openapi",
                version = "1.0.0"
        ),
        servers = {
                @Server(
                        url = "{schema}://localhost:8080",
                        variables = @ServerVariable(
                                name = "schema",
                                allowableValues = {"http"},
                                defaultValue = "http"
                        )
                )
        }
)
public class OpenApiConfig {
}
