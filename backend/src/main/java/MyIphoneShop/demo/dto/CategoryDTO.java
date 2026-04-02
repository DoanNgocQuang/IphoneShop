package MyIphoneShop.demo.dto;

import MyIphoneShop.demo.entity.Category;
import lombok.Data;

@Data
public class CategoryDTO {
    private Integer categoryId;
    private String name;
    private Integer parentId; // Dành cho danh mục con (VD: Áo Thun thuộc danh mục Áo)

    // Hàm tiện ích ép kiểu từ Entity sang DTO
    public static CategoryDTO fromEntity(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryId(category.getCategoryId());
        dto.setName(category.getName());

        // Nếu danh mục này là danh mục con, lấy ID của danh mục cha
        if (category.getParentCategory() != null) {
            dto.setParentId(category.getParentCategory().getCategoryId());
        }
        return dto;
    }
}