package MyIphoneShop.demo.service;

import MyIphoneShop.demo.dto.CategoryDTO;
import MyIphoneShop.demo.entity.Category;
import MyIphoneShop.demo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    // 1. API Lấy danh sách toàn bộ danh mục (Cho Dropdown Frontend)
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 2. API Thêm danh mục mới (Cho Admin)
    public String createCategory(CategoryDTO request) {
        Category category = new Category();
        category.setName(request.getName());

        if (request.getParentId() != null) {
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục cha mã #" + request.getParentId()));
            category.setParentCategory(parent);
        }

        categoryRepository.save(category);
        return "Tạo danh mục '" + request.getName() + "' thành công!";
    }

    // ==========================================
    // 3. API Sửa Danh Mục (MỚI THÊM)
    // ==========================================
    public String updateCategory(Integer id, CategoryDTO request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục mã #" + id));

        // Cập nhật tên mới
        category.setName(request.getName());

        // Cập nhật danh mục cha (nếu có sự thay đổi)
        if (request.getParentId() != null) {
            // Chống lỗi ngớ ngẩn: Admin set danh mục cha là chính nó
            if (request.getParentId().equals(id)) {
                throw new RuntimeException("Một danh mục không thể làm cha của chính nó!");
            }
            Category parent = categoryRepository.findById(request.getParentId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục cha mã #" + request.getParentId()));
            category.setParentCategory(parent);
        } else {
            // Nếu Front-end gửi parentId = null -> Xóa liên kết cha con cũ
            category.setParentCategory(null);
        }

        categoryRepository.save(category);
        return "Cập nhật danh mục #" + id + " thành công!";
    }

    // ==========================================
    // 4. API Xóa Danh Mục (MỚI THÊM)
    // ==========================================
    public String deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục mã #" + id));

        try {
            categoryRepository.delete(category);
            return "Đã xóa danh mục thành công!";
        } catch (Exception e) {
            // Bắt lỗi nếu danh mục này đang chứa sản phẩm hoặc chứa danh mục con khác
            throw new RuntimeException("Không thể xóa! Danh mục này đang chứa sản phẩm hoặc có danh mục con bên trong. Vui lòng xóa hết sản phẩm trước!");
        }
    }
}