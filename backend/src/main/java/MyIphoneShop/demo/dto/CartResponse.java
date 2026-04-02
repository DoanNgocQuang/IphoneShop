package MyIphoneShop.demo.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class CartResponse {
    private Integer cartId;
    private Integer userId;
    private List<CartItemDTO> items; // Danh sách các món đồ
    private BigDecimal totalCartPrice; // Tổng tiền cả giỏ hàng
}