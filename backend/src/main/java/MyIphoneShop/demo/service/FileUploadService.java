package MyIphoneShop.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {

    // Thư mục chứa ảnh sẽ tự động được tạo ra trong project của bạn
    private final String UPLOAD_DIR = "uploads/";

    public String storeFile(MultipartFile file) {
        try {
            // 1. Tạo thư mục nếu chưa tồn tại
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 2. Lấy tên file gốc (VD: ao-khoac.jpg)
            String originalFileName = file.getOriginalFilename();

            // 3. Đổi tên file để không bao giờ bị trùng (VD: 123e4567-e89b-12d3_ao-khoac.jpg)
            String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;

            // 4. Lưu file vào ổ cứng
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath);

            // 5. Trả về đường dẫn để lưu vào Database (Tí nữa Front-end sẽ dùng link này để hiển thị)
            return "/uploads/" + uniqueFileName;

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu file: " + e.getMessage());
        }
    }
}