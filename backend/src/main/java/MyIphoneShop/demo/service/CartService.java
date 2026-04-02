package MyIphoneShop.demo.service;

import MyIphoneShop.demo.dto.CartItemDTO;
import MyIphoneShop.demo.dto.CartResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import MyIphoneShop.demo.dto.AddToCartRequest;
import MyIphoneShop.demo.entity.Cart;
import MyIphoneShop.demo.entity.CartDetail;
import MyIphoneShop.demo.entity.IphoneVariant;
import MyIphoneShop.demo.entity.User;
import MyIphoneShop.demo.repository.CartDetailRepository;
import MyIphoneShop.demo.repository.CartRepository;
import MyIphoneShop.demo.repository.IphoneVariantRepository;
import MyIphoneShop.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartDetailRepository cartDetailRepository;
    private final UserRepository userRepository;
    private final IphoneVariantRepository variantRepository;

    @Transactional
    // 1. Thêm tham số 'String email' vào hàm
    public String addToCart(String email, AddToCartRequest request) {

        // 2. SỚ lại cách tìm User: dùng findByEmail thay vì findById
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
        // 2. Kiểm tra Biến thể (iPhone model nào) có tồn tại và đủ tồn kho không
        IphoneVariant variant = variantRepository.findById(request.getVariantId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại!"));

        if (variant.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Kho không đủ hàng! Chỉ còn " + variant.getStockQuantity() + " chiếc.");
        }

        // 3. Tìm giỏ hàng của khách, nếu khách chưa có giỏ thì tạo mới luôn
        Cart cart = cartRepository.findByUser_UserId(user.getUserId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        // 4. Kiểm tra xem sản phẩm này đã có trong giỏ hàng chưa
        Optional<CartDetail> existingDetail = cartDetailRepository.findByCart_CartId(cart.getCartId())
                .stream()
                .filter(detail -> detail.getVariant().getVariantId().equals(variant.getVariantId()))
                .findFirst();

        if (existingDetail.isPresent()) {
            // Nếu khách đã từng thêm áo này vào giỏ -> Cộng dồn số lượng lên
            CartDetail detail = existingDetail.get();
            int newQuantity = detail.getQuantity() + request.getQuantity();

            // Kiểm tra lại tồn kho sau khi cộng dồn
            if (variant.getStockQuantity() < newQuantity) {
                throw new RuntimeException("Tổng số lượng trong giỏ vượt quá tồn kho!");
            }
            detail.setQuantity(newQuantity);
            cartDetailRepository.save(detail);
        } else {
            // Nếu áo chưa có trong giỏ -> Tạo dòng chi tiết mới
            CartDetail newDetail = new CartDetail();
            newDetail.setCart(cart);
            newDetail.setVariant(variant);
            newDetail.setQuantity(request.getQuantity());
            cartDetailRepository.save(newDetail);
        }

        return "Thêm thành công " + request.getQuantity() + " sản phẩm vào giỏ hàng!";
    }

    // Đổi tên hàm và nhận vào email thay vì userId
    public CartResponse getCartByEmail(String email) {
        // 1. Tìm User từ Database bằng email (Lấy từ Token)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        // Rút cái userId ra để xài cho các dòng code bên dưới
        Integer userId = user.getUserId();

        // 2. Lấy giỏ hàng của User (Logic cũ của bạn, không đổi 1 chữ nào)
        Cart cart = cartRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng trống hoặc người dùng chưa có giỏ hàng!"));

        // 3. Lấy danh sách các món đồ trong giỏ
        List<CartDetail> details = cartDetailRepository.findByCart_CartId(cart.getCartId());

        CartResponse response = new CartResponse();
        response.setCartId(cart.getCartId());
        response.setUserId(userId);

        BigDecimal totalCartPrice = BigDecimal.ZERO;
        List<CartItemDTO> itemDTOs = new ArrayList<>();

        // 4. Duyệt qua từng món đồ, map dữ liệu và tính tiền
        for (CartDetail detail : details) {
            CartItemDTO item = new CartItemDTO();
            item.setCartDetailId(detail.getCartDetailId());
            item.setVariantId(detail.getVariant().getVariantId());

            // Lấy tên iPhone từ bảng Iphone thông qua Variant
            item.setProductName(detail.getVariant().getIphone().getName());
            item.setColor(detail.getVariant().getColor());
            item.setSize(detail.getVariant().getSize());
            item.setPrice(detail.getVariant().getPrice());
            item.setQuantity(detail.getQuantity());

            // Tính thành tiền cho món đồ này: Giá * Số lượng
            BigDecimal itemTotal = detail.getVariant().getPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
            item.setItemTotal(itemTotal);

            // Cộng dồn vào tổng tiền cả giỏ
            totalCartPrice = totalCartPrice.add(itemTotal);

            itemDTOs.add(item);
        }

        response.setItems(itemDTOs);
        response.setTotalCartPrice(totalCartPrice);

        return response;
    }

    // 3. API CẬP NHẬT SỐ LƯỢNG SẢN PHẨM TRONG GIỎ
    @Transactional
    public String updateCartItem(String email, Integer variantId, Integer newQuantity) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
        Cart cart = cartRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại!"));

        // Tìm các món trong giỏ
        List<CartDetail> cartDetails = cartDetailRepository.findByCart_CartId(cart.getCartId());

        CartDetail targetItem = cartDetails.stream()
                .filter(item -> item.getVariant().getVariantId().equals(variantId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sản phẩm không có trong giỏ hàng!"));

        // Nếu khách giảm số lượng về 0 -> Tự động xóa luôn khỏi giỏ
        if (newQuantity <= 0) {
            cartDetailRepository.delete(targetItem);
            return "Đã xóa sản phẩm khỏi giỏ hàng!";
        }

        // Kiểm tra xem kho còn đủ hàng không
        if (targetItem.getVariant().getStockQuantity() < newQuantity) {
            throw new RuntimeException(
                    "Số lượng tồn kho không đủ! Chỉ còn " + targetItem.getVariant().getStockQuantity() + " sản phẩm.");
        }

        targetItem.setQuantity(newQuantity);
        cartDetailRepository.save(targetItem);
        return "Cập nhật số lượng thành công!";
    }

    // 4. API XÓA HẲN MỘT SẢN PHẨM KHỎI GIỎ
    @Transactional
    public String removeCartItem(String email, Integer variantId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));
        Cart cart = cartRepository.findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại!"));

        List<CartDetail> cartDetails = cartDetailRepository.findByCart_CartId(cart.getCartId());

        CartDetail targetItem = cartDetails.stream()
                .filter(item -> item.getVariant().getVariantId().equals(variantId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sản phẩm không có trong giỏ hàng!"));

        cartDetailRepository.delete(targetItem);
        return "Đã xóa sản phẩm khỏi giỏ hàng!";
    }
}