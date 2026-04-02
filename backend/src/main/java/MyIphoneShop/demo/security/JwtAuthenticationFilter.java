package MyIphoneShop.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Lấy thông tin từ Header của Request gửi lên
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // 2. Kiểm tra xem có Token không (Token chuẩn luôn bắt đầu bằng chữ "Bearer ")
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Không có thẻ -> Cho đi tiếp (Sẽ bị chặn ở cổng sau nếu API đó yêu cầu bảo mật)
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Cắt lấy chuỗi Token (bỏ chữ "Bearer " gồm 7 ký tự)
        jwt = authHeader.substring(7);

        // 4. Giải mã Token để lấy Email (dùng Máy in Token đã tạo ở bài trước)
        userEmail = jwtUtils.extractEmail(jwt);

        // 5. Nếu có Email và người này chưa được xác thực trong bộ nhớ tạm
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Lấy thông tin người dùng từ Database lên
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 6. Kiểm tra xem Token còn hạn và có đúng của người này không
            if (jwtUtils.isTokenValid(jwt, userDetails)) {
                // Hợp lệ -> Cấp "Quyền đi qua"
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Lưu vào bộ nhớ để Controller phía sau biết ai đang gọi API
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Cho phép đi tiếp vào Controller
        filterChain.doFilter(request, response);
    }
}