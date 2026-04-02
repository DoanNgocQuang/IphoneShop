package MyIphoneShop.demo.service;

import MyIphoneShop.demo.dto.UserDTO;
import MyIphoneShop.demo.entity.User;
import MyIphoneShop.demo.entity.enums.UserStatus;
import MyIphoneShop.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    // 1. Lấy danh sách toàn bộ khách hàng
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 2. Tìm kiếm khách hàng (Gọi hàm @Query rõ ràng đã tạo ở File 1)
    public List<UserDTO> searchUsers(String keyword) {
        return userRepository.searchByKeyword(keyword).stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // 3. Khóa hoặc Mở khóa tài khoản
    @Transactional
    public String toggleUserStatus(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng mã #" + userId));

        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> role.getRoleName().equals("ADMIN"));
        if (isAdmin) {
            throw new RuntimeException("Không thể khóa tài khoản của Quản trị viên!");
        }

        if (user.getStatus() == UserStatus.ACTIVE) {
            user.setStatus(UserStatus.LOCKED);
            userRepository.save(user);

            // Gửi email thông báo khóa tài khoản
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(user.getEmail());
                message.setSubject("Thông báo: Tài khoản của bạn đã bị khóa - My iPhone Shop");
                message.setText("Xin chào " + user.getFullName() + ",\n\n"
                        + "Tài khoản của bạn trên hệ thống My iPhone Shop đã bị Quản trị viên khóa do phát hiện bất thường hoặc vi phạm chính sách của cửa hàng.\n\n"
                        + "Nếu bạn cho rằng đây là một sự nhầm lẫn, vui lòng liên hệ với bộ phận hỗ trợ của chúng tôi để được giải quyết.\n\n"
                        + "Trân trọng,\nĐội ngũ My iPhone Shop.");
                mailSender.send(message);
            } catch (Exception e) {
                System.err.println("Lỗi khi gửi email thông báo khóa tài khoản: " + e.getMessage());
            }

            return "Đã KHÓA tài khoản của: " + user.getEmail();
        } else {
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);
            return "Đã MỞ KHÓA tài khoản của: " + user.getEmail();
        }
    }
}