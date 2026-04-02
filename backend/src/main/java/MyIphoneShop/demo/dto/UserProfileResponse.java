package MyIphoneShop.demo.dto;

import MyIphoneShop.demo.entity.User;
import lombok.Data;

@Data
public class UserProfileResponse {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    public static UserProfileResponse fromEntity(User user) {
        UserProfileResponse dto = new UserProfileResponse();
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        return dto;
    }
}