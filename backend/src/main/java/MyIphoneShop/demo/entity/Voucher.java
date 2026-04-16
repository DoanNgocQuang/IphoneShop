package MyIphoneShop.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "`Voucher`")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private Integer voucherId;

    @Column(name = "code", unique = true, nullable = false, length = 50)
    private String code;

    // Giảm theo % (VD: 10 nghĩa là 10%)
    @Column(name = "discount_percent", precision = 5, scale = 2)
    private BigDecimal discountPercent;

    // Số tiền giảm tối đa (VD: giảm tối đa 50k)
    @Column(name = "max_discount", precision = 15, scale = 2)
    private BigDecimal maxDiscount;

    // Giá trị đơn hàng tối thiểu để áp dụng Voucher
    @Column(name = "min_order_value", precision = 15, scale = 2)
    private BigDecimal minOrderValue;

    // Giới hạn lượt dùng của Voucher
    @Column(name = "usage_limit")
    private Integer usageLimit;

    // Số lượt đã dùng
    @Column(name = "used_count")
    private Integer usedCount = 0;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    // Bật/tắt voucher
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}
