package MyIphoneShop.demo.repository;

import MyIphoneShop.demo.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {
    // Tìm giỏ hàng của một user
    Optional<Cart> findByUser_UserId(Integer userId);
}