package MyIphoneShop.demo.dto;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Integer variantId; // Mua cụ thể áo nào (Mã biến thể chứa size/màu)
    private Integer quantity;  // Số lượng bao nhiêu cái
}