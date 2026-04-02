package MyIphoneShop.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ánh xạ đường dẫn "/uploads/**" vào thư mục "uploads/" trong máy
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}