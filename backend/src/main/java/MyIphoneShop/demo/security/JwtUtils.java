package MyIphoneShop.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // 1. Hàm này dùng để TẠO RA TOKEN khi user đăng nhập thành công
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email) // Lưu email vào token
                .issuedAt(new Date(System.currentTimeMillis())) // Thời gian tạo
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Thời gian hết hạn (24h)
                .signWith(getSignInKey()) // Ký tên bằng chuỗi bí mật
                .compact();
    }

    // 2. Hàm này dùng để LẤY EMAIL TỪ TOKEN khi user gửi request lên
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 3. Hàm kiểm tra xem Token còn hạn và có đúng của User đó không
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }

    // Biến đổi chuỗi bí mật trong file properties thành Key bảo mật
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}