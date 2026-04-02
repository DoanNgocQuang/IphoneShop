package MyIphoneShop.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Iphone_Image")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IphoneImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Integer imageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iphone_id", nullable = false)
    private Iphone iphone;

    @Column(name = "image_url", nullable = false, columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "is_thumbnail")
    private Boolean isThumbnail = false;
}