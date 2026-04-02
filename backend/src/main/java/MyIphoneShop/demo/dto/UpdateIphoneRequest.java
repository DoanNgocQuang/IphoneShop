package MyIphoneShop.demo.dto;

import MyIphoneShop.demo.entity.enums.Color;
import MyIphoneShop.demo.entity.enums.Size;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateIphoneRequest {
    private String name;
    private String description;
    private Integer categoryId;
    private BigDecimal price;      // Thêm dòng này để hết lỗi getPrice
    private List<String> imageUrls;
    private List<VariantDetail> variants;

    @Data
    public static class VariantDetail {
        private Integer variantId;
        private String sku;
        private Color color;
        private Size size;
        private BigDecimal price;
        private Integer stockQuantity;
    }
}