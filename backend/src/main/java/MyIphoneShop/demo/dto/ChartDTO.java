package MyIphoneShop.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ChartDTO {
    private String label;      // Nhãn: Ví dụ "Ngày 15/10", "Tháng 10", "Năm 2024"
    private BigDecimal value;  // Cột giá trị: Tổng doanh thu
}