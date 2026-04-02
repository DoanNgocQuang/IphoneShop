package MyIphoneShop.demo.repository;

import MyIphoneShop.demo.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    // Xem chi tiết các món đồ trong 1 đơn hàng (để in hóa đơn)
    List<OrderDetail> findByOrder_OrderId(Integer orderId);
}