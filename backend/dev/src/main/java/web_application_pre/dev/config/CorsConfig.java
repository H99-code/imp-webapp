package web_application_pre.dev.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;





@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        System.out.println("CORS Configuration Loaded!");
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        //.allowedOrigins("http://localhost:3000")
                        .allowedOrigins("https://impiii.netlify.app")
                        //.allowedOrigins("http://localhost:8080")
                        .allowedOrigins("https://5ade-2a01-41e3-2320-2500-41b4-734d-a746-9d87.ngrok-free.app")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}


