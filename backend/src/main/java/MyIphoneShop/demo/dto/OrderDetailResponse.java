package MyIphoneShop.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderDetailResponse {
    private OrderResponse orderInfo; // Tận dụng luôn cái DTO OrderResponse vừa tạo lúc nãy
    private List<OrderItemDTO> items; // Danh sách các món quần áo trong đơn
}