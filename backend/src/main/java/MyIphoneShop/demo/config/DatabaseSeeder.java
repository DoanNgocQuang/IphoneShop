package MyIphoneShop.demo.config;

import MyIphoneShop.demo.entity.Role;
import MyIphoneShop.demo.entity.User;
import MyIphoneShop.demo.repository.RoleRepository;
import MyIphoneShop.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository; // Nếu chưa có file này thì bạn tạo một cái interface trống nhé
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Kiểm tra xem admin đã tồn tại chưa
        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {

            // Lấy Role ADMIN từ Database (ID = 1 vì trong SQL đã có sẵn)
            Role adminRole = roleRepository.findById(1)
                    .orElseThrow(() -> new RuntimeException("Chưa có Role ADMIN trong DB!"));

            // 2. Tạo đối tượng Admin mới
            User admin = new User();
            admin.setFullName("Quản trị viên Hệ thống");
            admin.setEmail("admin@gmail.com");

            // ========================================================
            // ĐÂY LÀ CHÌA KHÓA: Tự động mã hóa mật khẩu bằng PasswordEncoder của chính bạn
            admin.setPasswordHash(passwordEncoder.encode("123456"));
            // Lưu ý: Nếu entity User của bạn đặt tên biến mật khẩu là 'password' thì sửa
            // lại thành admin.setPassword(...) nhé
            // ========================================================

            admin.setPhoneNumber("0988888888");

            // 3. Gắn quyền ADMIN cho user này
            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles); // Lưu ý: entity User của bạn phải có biến Set<Role> roles nhé

            // 4. Lưu xuống Database
            userRepository.save(admin);

            System.out.println("=========================================");
            System.out.println("ĐÃ TẠO TÀI KHOẢN ADMIN TỰ ĐỘNG TỪ CODE!");
            System.out.println("Email: admin@gmail.com");
            System.out.println("Mật khẩu: 123456");
            System.out.println("=========================================");
        }
    }
}