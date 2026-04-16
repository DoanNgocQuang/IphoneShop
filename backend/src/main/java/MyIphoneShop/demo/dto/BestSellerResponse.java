package MyIphoneShop.demo.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BestSellerResponse {
    private Integer iphoneId;
    private String name;
    private String description;
    private String categoryName;
    private BigDecimal price;
    private List<String> imageUrls;
    private Long totalSold; // Tổng số lượng đã bán
}
