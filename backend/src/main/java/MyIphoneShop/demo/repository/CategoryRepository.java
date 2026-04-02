package MyIphoneShop.demo.repository;

import MyIphoneShop.demo.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Lấy danh sách các danh mục gốc (Áo Nam, Quần Nam...)
    List<Category> findByParentCategoryIsNull();
}