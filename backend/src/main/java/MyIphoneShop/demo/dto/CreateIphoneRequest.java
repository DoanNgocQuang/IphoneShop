package MyIphoneShop.demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateIphoneRequest {
    private String name;
    private String description;
    private Integer categoryId; // Thuộc danh mục nào
    private List<VariantRequest> variants; // 1 iPhone có nhiều biến thể
    private BigDecimal price;
    // Thêm danh sách chứa các link ảnh
    private List<String> imageUrls;
}