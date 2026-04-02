package MyIphoneShop.demo.controller;

import MyIphoneShop.demo.dto.AddToCartRequest;
import MyIphoneShop.demo.dto.CartResponse;
import MyIphoneShop.demo.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody AddToCartRequest request, Principal principal) {
        // Tự động lấy Email từ Token
        String email = principal.getName();

        // Truyền email và request xuống Service
        String result = cartService.addToCart(email, request);

        return ResponseEntity.ok(result);
    }
    @GetMapping
    public ResponseEntity<?> getCart(Principal principal) {
        // 1. Rút thẻ (Token) lấy email
        String email = principal.getName();

        // 2. Gọi hàm mới tạo
        CartResponse response = cartService.getCartByEmail(email);

        return ResponseEntity.ok(response);
    }
    // Nối tiếp các hàm cũ trong CartController của bạn...

    // API Cập nhật số lượng
    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(
            Principal principal,
            @RequestParam Integer variantId,
            @RequestParam Integer quantity) {
        try {
            String email = principal.getName();
            return ResponseEntity.ok(cartService.updateCartItem(email, variantId, quantity));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // API Xóa sản phẩm khỏi giỏ
    @DeleteMapping("/remove/{variantId}")
    public ResponseEntity<?> removeCartItem(Principal principal, @PathVariable Integer variantId) {
        try {
            String email = principal.getName();
            return ResponseEntity.ok(cartService.removeCartItem(email, variantId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}