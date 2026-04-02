package MyIphoneShop.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Iphone")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Iphone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iphone_id")
    private Integer iphoneId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false; // Tính năng Soft Delete

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "iphone", cascade = CascadeType.ALL)
    private List<IphoneVariant> variants;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }
}