package MyIphoneShop.demo.controller;

import MyIphoneShop.demo.dto.ChangePasswordRequest;
import MyIphoneShop.demo.dto.UpdateProfileRequest;
import MyIphoneShop.demo.dto.UserProfileResponse;
import MyIphoneShop.demo.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    // Lấy thông tin cá nhân của người đang đăng nhập
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(userProfileService.getMyProfile(email));
    }

    // Cập nhật thông tin cá nhân
    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(Principal principal, @RequestBody UpdateProfileRequest request) {
        try {
            String email = principal.getName();
            return ResponseEntity.ok(userProfileService.updateProfile(email, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    // API Đổi mật khẩu
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(Principal principal, @RequestBody ChangePasswordRequest request) {
        try {
            String email = principal.getName(); // Tự động lấy email từ token
            return ResponseEntity.ok(userProfileService.changePassword(email, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}