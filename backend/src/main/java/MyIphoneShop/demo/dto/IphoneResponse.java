package MyIphoneShop.demo.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class IphoneResponse {
    private Integer iphoneId;
    private String name;
    private String description;
    private Integer categoryId;    // Thêm dòng này
    private String categoryName;
    private BigDecimal price;      // Thêm dòng này để hết lỗi setPrice
    private List<String> imageUrls;
    private List<VariantDTO> variants;
}