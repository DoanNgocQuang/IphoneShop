package MyIphoneShop.demo.repository;

import MyIphoneShop.demo.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Integer> {
    // Lấy tất cả các món đồ đang nằm trong giỏ hàng cụ thể
    List<CartDetail> findByCart_CartId(Integer cartId);
}