package MyIphoneShop.demo.repository;

import MyIphoneShop.demo.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    // Xem chi tiết các món đồ trong 1 đơn hàng (để in hóa đơn)
    List<OrderDetail> findByOrder_OrderId(Integer orderId);

    // Thống kê sản phẩm bán chạy: Gom nhóm theo iPhone ID, cộng tổng số lượng đã bán, sắp xếp giảm dần
    @Query("SELECT od.variant.iphone.iphoneId, SUM(od.quantity) AS totalSold " +
           "FROM OrderDetail od " +
           "GROUP BY od.variant.iphone.iphoneId " +
           "ORDER BY totalSold DESC")
    List<Object[]> findTopSellingIphoneIds(Pageable pageable);
}