package MyIphoneShop.demo.dto;

import MyIphoneShop.demo.entity.enums.Color;
import MyIphoneShop.demo.entity.enums.Size;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemDTO {
    private Integer cartDetailId; // ID của dòng giỏ hàng (để sau này làm chức năng Xóa khỏi giỏ)
    private Integer variantId;    // Mã biến thể
    private String productName;   // Tên áo
    private Color color;         // Màu sắc
    private Size size;          // Kích cỡ
    private BigDecimal price;     // Giá 1 chiếc
    private Integer quantity;     // Số lượng mua
    private BigDecimal itemTotal; // Thành tiền (Giá x Số lượng)
}