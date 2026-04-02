package MyIphoneShop.demo.dto;

import MyIphoneShop.demo.entity.enums.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderResponse {
    private Integer orderId;
    private String customerName; // Tên người đặt
    private String customerEmail;
    private BigDecimal totalAmount;
    private OrderStatus status; // Chuẩn Enum nhé
    private LocalDateTime orderDate;
    private String shippingAddress;
    private String note;
}