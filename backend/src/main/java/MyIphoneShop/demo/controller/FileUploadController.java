package MyIphoneShop.demo.controller;

import MyIphoneShop.demo.service.FileUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;

    // API Upload ảnh (Swagger sẽ tự động vẽ ra nút Upload File)
    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Trả về cái đường dẫn (VD: /uploads/ten-anh.jpg) để bạn dán vào DTO Thêm Áo
            String fileUrl = fileUploadService.storeFile(file);
            return ResponseEntity.ok(fileUrl);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}