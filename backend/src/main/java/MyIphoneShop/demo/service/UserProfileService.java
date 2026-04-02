package MyIphoneShop.demo.service;

import MyIphoneShop.demo.dto.ChangePasswordRequest;
import MyIphoneShop.demo.dto.UpdateProfileRequest;
import MyIphoneShop.demo.dto.UserProfileResponse;
import MyIphoneShop.demo.entity.Address;
import MyIphoneShop.demo.entity.User;
import MyIphoneShop.demo.repository.AddressRepository;
import MyIphoneShop.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository; // Gọi thợ xây Address vào đây

    // 1. API: Xem thông tin cá nhân
    public UserProfileResponse getMyProfile(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        // Kéo danh sách địa chỉ của User này ra
        List<Address> addresses = addressRepository.findByUser_UserId(user.getUserId());
        String userAddress = "";

        if (!addresses.isEmpty()) {
            // Lọc lấy cái địa chỉ mặc định, nếu không có thì lấy cái đầu tiên trong danh sách
            Address defaultAddress = addresses.stream()
                    .filter(a -> Boolean.TRUE.equals(a.getIsDefault()))
                    .findFirst()
                    .orElse(addresses.get(0));
            userAddress = defaultAddress.getShippingAddress(); // Lấy cột street gửi ra cho Web
        }

        // Đóng gói dữ liệu gửi về cho Frontend
        UserProfileResponse response = new UserProfileResponse();
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setAddress(userAddress); // Nhét địa chỉ vào đây

        return response;
    }

    // 2. API: Cập nhật thông tin cá nhân và lưu Địa chỉ
    @Transactional
    public String updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        // --- BƯỚC 1: LƯU VÀO BẢNG USER ---
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        userRepository.save(user);

        // --- BƯỚC 2: LƯU VÀO BẢNG ADDRESS ---
        if (request.getAddress() != null && !request.getAddress().trim().isEmpty()) {
            List<Address> addresses = addressRepository.findByUser_UserId(user.getUserId());
            Address addressToUpdate;

            if (!addresses.isEmpty()) {
                // Nếu đã có địa chỉ cũ, tìm cái mặc định để ghi đè lên
                addressToUpdate = addresses.stream()
                        .filter(a -> Boolean.TRUE.equals(a.getIsDefault()))
                        .findFirst()
                        .orElse(addresses.get(0));
            } else {
                // Nếu khách này mới tinh chưa có địa chỉ nào, tạo mới
                addressToUpdate = new Address();
                addressToUpdate.setUser(user);
                // Bảng của Hải yêu cầu city ko được null, nên set cứng 1 chữ tránh lỗi Database
                addressToUpdate.setShippingAddress("Chưa cập nhật");
            }

            // Lưu toàn bộ nội dung khách gõ trên Web vào cột street
            addressToUpdate.setShippingAddress(request.getAddress());
            addressToUpdate.setRecipientName(user.getFullName());
            addressToUpdate.setPhoneNumber(user.getPhoneNumber());
            addressToUpdate.setIsDefault(true); // Đặt làm mặc định luôn

            addressRepository.save(addressToUpdate);
        }

        return "Cập nhật hồ sơ và địa chỉ thành công!";
    }

    // 3. API: Đổi mật khẩu (Giữ nguyên code xịn của Hải)
    @Transactional
    public String changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Mật khẩu cũ không chính xác!");
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp!");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return "Đổi mật khẩu thành công! Lần đăng nhập sau hãy dùng mật khẩu mới nhé.";
    }
}