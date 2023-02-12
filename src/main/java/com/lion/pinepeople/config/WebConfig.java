package com.lion.pinepeople.config;

import com.lion.pinepeople.domain.annotation.LoginMemberIdArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.CacheControl;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    private final LoginMemberIdArgumentResolver loginMemberIdArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberIdArgumentResolver);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/templates/", "classpath:/static/");
//                .addResourceLocations("file:src/main/resources/templates/", "file:src/main/resources/static/");
    }

}

//@Configuration
//public class WebConfig extends WebMvcConfigurationSupport {
//
//    @Profile("prod")
//    @Configuration
//    public static class ProdMvcConfiguration implements WebMvcConfigurer {
//
//        @Override
//        public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//            registry.addResourceHandler("/**")
//                    .addResourceLocations("classpath:/templates/", "classpath:/static/")
//                    .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
//        }
//    }
//
//    @Profile("local")
//    @Configuration
//    public static class LocalMvcConfiguration implements WebMvcConfigurer {
//
//        @Override
//        public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//            registry.addResourceHandler("/**")
////                    .addResourceLocations("file:src/main/resources/templates/", "file:src/main/resources/static/");
//                    .addResourceLocations("classpath:/templates/", "classpath:/static/");
//        }
//    }
//
//}
