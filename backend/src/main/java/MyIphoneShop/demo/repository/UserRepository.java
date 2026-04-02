package MyIphoneShop.demo.repository;

import MyIphoneShop.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Spring Boot tự động dịch hàm này thành: SELECT * FROM User WHERE email = ?
    Optional<User> findByEmail(String email);

    // Spring Boot tự động dịch thành hàm kiểm tra tồn tại để Validate lúc Đăng ký
    boolean existsByEmail(String email);
    // Hàm tìm kiếm người dùng theo tên hoặc email
    @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<User> searchByKeyword(@Param("keyword") String keyword);
    // Dùng @Query để chỉ đích danh: Tìm User, nối (JOIN) với bảng Role, lọc ra những người có tên quyền là CUSTOMER
    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.roleName = 'CUSTOMER'")
    long countCustomers();
}