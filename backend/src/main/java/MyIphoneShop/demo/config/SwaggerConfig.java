package MyIphoneShop.demo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "My iPhone Shop API", version = "1.0"), security = @SecurityRequirement(name = "bearerAuth") // Yêu
                                                                                                                                     // cầu
                                                                                                                                     // ổ
                                                                                                                                     // khóa
                                                                                                                                     // cho
                                                                                                                                     // toàn
                                                                                                                                     // bộ
                                                                                                                                     // API
)
@SecurityScheme(name = "bearerAuth", description = "Nhập JWT Token vào đây", scheme = "bearer", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class SwaggerConfig {
}