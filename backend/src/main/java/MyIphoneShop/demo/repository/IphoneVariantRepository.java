package MyIphoneShop.demo.repository;

import MyIphoneShop.demo.entity.IphoneVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IphoneVariantRepository extends JpaRepository<IphoneVariant, Integer> {
    // Thêm dòng này vào trong interface
    List<IphoneVariant> findByIphone_IphoneId(Integer iphoneId);
}