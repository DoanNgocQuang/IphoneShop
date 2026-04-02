package MyIphoneShop.demo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import MyIphoneShop.demo.entity.Iphone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IphoneRepository extends JpaRepository<Iphone, Integer> {

    // Chỉ lấy những sản phẩm đang được bán (is_deleted = false)
    List<Iphone> findByIsDeletedFalse();
    // 1. Tìm kiếm iPhone theo tên (gõ chữ thường/chữ hoa đều tìm được)
    List<Iphone> findByNameContainingIgnoreCaseAndIsDeletedFalse(String keyword);

    // 2. Lọc danh sách iPhone theo ID của Danh mục (Ví dụ: Lọc riêng iPhone 12)
    List<Iphone> findByCategory_CategoryIdAndIsDeletedFalse(Integer categoryId);
    // Tìm iPhone thuộc Category hiện tại HOẶC thuộc các Category con của nó
    @Query("SELECT c FROM Iphone c WHERE (c.category.categoryId = :id OR c.category.parentCategory.categoryId = :id) AND c.isDeleted = false")
    List<Iphone> findByCategoryIdOrParentId(@Param("id") Integer id);
    // 3. Đếm tổng số lượng iPhone đang bán (isDeleted = false)
    long countByIsDeletedFalse();
}