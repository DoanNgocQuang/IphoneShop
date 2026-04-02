package MyIphoneShop.demo.repository;

import MyIphoneShop.demo.entity.IphoneImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IphoneImageRepository extends JpaRepository<IphoneImage, Integer> {
    // Lấy danh sách ảnh của 1 sản phẩm
    List<IphoneImage> findByIphone_IphoneId(Integer iphoneId);

}