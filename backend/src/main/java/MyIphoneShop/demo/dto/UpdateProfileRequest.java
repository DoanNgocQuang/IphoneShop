package MyIphoneShop.demo.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String fullName;
    private String phoneNumber;

    // Thêm biến này để hứng địa chỉ từ Frontend gửi lên
    private String address;
}