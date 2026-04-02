package MyIphoneShop.demo.dto;

import MyIphoneShop.demo.entity.enums.Color;
import MyIphoneShop.demo.entity.enums.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class VariantRequest {
    private String sku;            // Mã kho (VD: AK-DEN-M)
    private Color color;          // Màu sắc
    private Size size;           // Kích cỡ
    private BigDecimal price;      // Giá bán
    private Integer stockQuantity; // Số lượng nhập kho
}