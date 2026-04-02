package MyIphoneShop.demo.service;

import MyIphoneShop.demo.dto.AuthResponse; // Thêm DTO này
import MyIphoneShop.demo.dto.LoginRequest;
import MyIphoneShop.demo.dto.RegisterRequest;
import MyIphoneShop.demo.dto.ResetPasswordRequest;
import MyIphoneShop.demo.entity.Role;
import MyIphoneShop.demo.entity.User;
import MyIphoneShop.demo.entity.enums.UserStatus;
import MyIphoneShop.demo.repository.RoleRepository;
import MyIphoneShop.demo.repository.UserRepository;
import MyIphoneShop.demo.security.JwtUtils; // Thêm Máy in Token
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;
    // GỌI THÊM MÁY IN TOKEN VÀO ĐÂY:
    private final JwtUtils jwtUtils;

    public User registerUser(RegisterRequest request) {
        // 1. Kiểm tra Email đã tồn tại chưa
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng!");
        }

        // 2. Tạo đối tượng User mới
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        // Mã hóa mật khẩu trước khi lưu
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setStatus(UserStatus.ACTIVE);

        // 3. Gán quyền mặc định là CUSTOMER
        Role customerRole = roleRepository.findByRoleName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền CUSTOMER trong DB"));
        Set<Role> roles = new HashSet<>();
        roles.add(customerRole);
        user.setRoles(roles);

        // 4. Lưu vào Database
        return userRepository.save(user);
    }

    // ĐÃ SỬA: Trả về AuthResponse thay vì String
    public AuthResponse loginUser(LoginRequest request) {
        // 1. Tìm User theo Email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại trong hệ thống!"));

        // 2. Kiểm tra tài khoản có bị khóa không (Áp dụng Enum UserStatus)
        if (user.getStatus() == UserStatus.LOCKED) {
            throw new RuntimeException("Tài khoản của bạn đã bị khóa!");
        }

        // 3. So sánh mật khẩu gốc (người dùng nhập) với mật khẩu đã mã hóa trong DB
        boolean isPasswordMatch = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        if (!isPasswordMatch) {
            throw new RuntimeException("Sai mật khẩu!");
        }

        // 4. NẾU MỌI THỨ ĐÚNG CHUẨN -> IN TOKEN BẰNG JWTUTILS
        String jwtToken = jwtUtils.generateToken(user.getEmail());

        // Đóng gói Token và lời chào trả về
        AuthResponse response = new AuthResponse();
        response.setToken(jwtToken);
        response.setMessage("Đăng nhập thành công! Xin chào " + user.getFullName());

        return response;
    }

    public String generateAndSendOTP(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại trong hệ thống!"));

        // Tạo mã OTP 6 số ngẫu nhiên
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Set OTP vào user và cho thời hạn 5 phút
        user.setResetOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));
        userRepository.save(user);

        // Gửi email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mã xác nhận khôi phục mật khẩu - My iPhone Shop");
        message.setText("Xin chào " + user.getFullName() + ",\n\n"
                + "Mã OTP để khôi phục mật khẩu của bạn là: " + otp + "\n\n"
                + "Mã này sẽ hết hạn sau 5 phút. Vui lòng không chia sẻ cho người khác.");
        mailSender.send(message);

        return "Mã OTP đã được gửi đến email của bạn!";
    }

    // 2. API XÁC NHẬN ĐỔI MẬT KHẨU
    public String resetPassword(ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp!");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại!"));

        // Kiểm tra mã OTP
        if (user.getResetOtp() == null || !user.getResetOtp().equals(request.getOtp())) {
            throw new RuntimeException("Mã OTP không chính xác!");
        }

        // Kiểm tra hạn sử dụng của OTP
        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Mã OTP đã hết hạn, vui lòng yêu cầu mã mới!");
        }

        // Cập nhật mật khẩu mới
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));

        // Dọn dẹp OTP sau khi dùng xong
        user.setResetOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);

        return "Lấy lại mật khẩu thành công! Bạn có thể đăng nhập bằng mật khẩu mới.";
    }
}