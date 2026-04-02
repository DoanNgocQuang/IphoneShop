package MyIphoneShop.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // Chấp nhận mọi Frontend (kể cả localhost hay 127.0.0.1)
        config.addAllowedHeader("*"); // Chấp nhận mọi Header chứa Token
        config.addAllowedMethod("*"); // Mở khóa TẤT CẢ các lệnh: PUT, GET, POST, DELETE, OPTIONS

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}