package MyIphoneShop.demo.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DashboardResponse {
    private BigDecimal totalRevenue; // Tổng tiền kiếm được
    private long totalOrders;        // Tổng số đơn hàng đã chốt
    private long pendingOrders;      // Đơn hàng đang chờ xử lý (cần Admin duyệt)
    private long totalProducts;      // Tổng số lượng mẫu áo đang bán
    private long totalCustomers;
}