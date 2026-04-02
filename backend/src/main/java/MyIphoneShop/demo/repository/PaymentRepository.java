package MyIphoneShop.demo.repository;

import MyIphoneShop.demo.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    // Tìm thông tin thanh toán dựa trên ID đơn hàng
    Optional<Payment> findByOrder_OrderId(Integer orderId);
}