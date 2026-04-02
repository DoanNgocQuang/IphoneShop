package MyIphoneShop.demo.dto;

import MyIphoneShop.demo.entity.User;
import MyIphoneShop.demo.entity.enums.UserStatus;
import lombok.Data;

@Data
public class UserDTO {
    private Integer userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private UserStatus status;
    private String role;

    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setStatus(user.getStatus());

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            dto.setRole(user.getRoles().iterator().next().getRoleName());
        }
        return dto;
    }
}