package MyIphoneShop.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Cart_Detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_detail_id")
    private Integer cartDetailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    // Liên kết với Variant thay vì Iphone
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false)
    private IphoneVariant variant;

    @Column(name = "quantity")
    private Integer quantity = 1;
}