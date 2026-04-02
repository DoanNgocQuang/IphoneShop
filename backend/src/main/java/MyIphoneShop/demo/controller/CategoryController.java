package MyIphoneShop.demo.controller;

import MyIphoneShop.demo.dto.CategoryDTO;
import MyIphoneShop.demo.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping("/admin/add")
    public ResponseEntity<?> createCategory(@RequestBody CategoryDTO request) {
        try {
            return ResponseEntity.ok(categoryService.createCategory(request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // MỞ CỔNG SỬA DANH MỤC
    @PutMapping("/admin/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody CategoryDTO request) {
        try {
            return ResponseEntity.ok(categoryService.updateCategory(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // MỞ CỔNG XÓA DANH MỤC
    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(categoryService.deleteCategory(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}