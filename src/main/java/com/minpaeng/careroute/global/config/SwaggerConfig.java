package com.minpaeng.careroute.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Configuration
public class SwaggerConfig {
    static {
        SpringDocUtils.getConfig()
                .addAnnotationsToIgnore(AuthenticationPrincipal.class);
    }

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("all")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public GroupedOpenApi memberApi() {
        return GroupedOpenApi.builder()
                .group("member")
                .pathsToMatch("/api/members/**")
                .build();
    }

    @Bean
    public GroupedOpenApi searchApi() {
        return GroupedOpenApi.builder()
                .group("route")
                .pathsToMatch("/api/route/**")
                .build();
    }

    @Bean
    public GroupedOpenApi routineApi() {
        return GroupedOpenApi.builder()
                .group("routine")
                .pathsToMatch("/api/routine/**")
                .build();
    }

    @Bean
    public GroupedOpenApi mypageApi() {
        return GroupedOpenApi.builder()
                .group("mypage")
                .pathsToMatch("/api/mypage/**")
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("API 명세서")
                        .description("Care Route API 명세서")
                        .version("v0.0.1"));
    }
}
