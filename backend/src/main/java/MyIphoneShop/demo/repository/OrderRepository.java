package MyIphoneShop.demo.repository;

import MyIphoneShop.demo.entity.Order;
import MyIphoneShop.demo.entity.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // 1. Tính tổng doanh thu (Chỉ cộng tiền những đơn hàng đã hoàn thành)
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'COMPLETED'")
    BigDecimal sumTotalRevenue();

    // 2. Đếm số lượng đơn hàng theo trạng thái (Ví dụ: đếm đơn PENDING)
    long countByStatus(OrderStatus status);
    // 1. Thống kê theo từng NGÀY (Cần Tháng và Năm)
    @Query("SELECT DAY(o.orderDate), SUM(o.totalAmount) FROM Order o " +
            "WHERE YEAR(o.orderDate) = :year AND MONTH(o.orderDate) = :month AND o.status = 'COMPLETED' " +
            "GROUP BY DAY(o.orderDate) ORDER BY DAY(o.orderDate)")
    List<Object[]> getRevenueByDay(@Param("year") int year, @Param("month") int month);

    // 2. Thống kê theo từng THÁNG (Chỉ cần Năm)
    @Query("SELECT MONTH(o.orderDate), SUM(o.totalAmount) FROM Order o " +
            "WHERE YEAR(o.orderDate) = :year AND o.status = 'COMPLETED' " +
            "GROUP BY MONTH(o.orderDate) ORDER BY MONTH(o.orderDate)")
    List<Object[]> getRevenueByMonth(@Param("year") int year);

    // 3. Thống kê theo NĂM
    @Query("SELECT YEAR(o.orderDate), SUM(o.totalAmount) FROM Order o " +
            "WHERE o.status = 'COMPLETED' " +
            "GROUP BY YEAR(o.orderDate) ORDER BY YEAR(o.orderDate)")
    List<Object[]> getRevenueByYear();
    // 1. Dành cho Khách hàng: Lấy các đơn của chính mình (sắp xếp mới nhất lên đầu)
    List<Order> findByUser_UserIdOrderByOrderDateDesc(Integer userId);

    // 2. Dành cho Admin: Lấy TOÀN BỘ đơn của shop (sắp xếp mới nhất lên đầu)
    List<Order> findAllByOrderByOrderDateDesc();
}