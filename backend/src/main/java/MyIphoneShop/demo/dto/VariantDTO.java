package MyIphoneShop.demo.dto;

import MyIphoneShop.demo.entity.enums.Color;
import MyIphoneShop.demo.entity.enums.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class VariantDTO {
    private Integer variantId;
    private Color color;
    private Size size;
    private BigDecimal price;
    private Integer stockQuantity;
}