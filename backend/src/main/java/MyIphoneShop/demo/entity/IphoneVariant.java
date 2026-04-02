package MyIphoneShop.demo.entity;

import MyIphoneShop.demo.entity.enums.Color;
import MyIphoneShop.demo.entity.enums.Size;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "Iphone_Variant")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IphoneVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variant_id")
    private Integer variantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iphone_id", nullable = false)
    private Iphone iphone;

    @Column(name = "sku", nullable = false, unique = true, length = 50)
    private String sku;

    @Enumerated(EnumType.STRING) // Bắt buộc phải là STRING để nó lưu dưới dạng chữ
    @Column(name = "color", length = 50)
    private Color color;

    @Enumerated(EnumType.STRING)
    @Column(name = "size")
    private Size size;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity")
    private Integer stockQuantity = 0;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false; // Tính năng Soft Delete
}