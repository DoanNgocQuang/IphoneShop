package MyIphoneShop.demo.service;

import MyIphoneShop.demo.dto.BestSellerResponse;
import MyIphoneShop.demo.dto.CreateIphoneRequest;
import MyIphoneShop.demo.dto.IphoneResponse;
import MyIphoneShop.demo.dto.UpdateIphoneRequest;
import MyIphoneShop.demo.dto.VariantDTO;
import MyIphoneShop.demo.entity.Category;
import MyIphoneShop.demo.entity.Iphone;
import MyIphoneShop.demo.entity.IphoneImage;
import MyIphoneShop.demo.entity.IphoneVariant;
import MyIphoneShop.demo.entity.enums.Color;
import MyIphoneShop.demo.entity.enums.Size;
import MyIphoneShop.demo.repository.CategoryRepository;
import MyIphoneShop.demo.repository.IphoneImageRepository;
import MyIphoneShop.demo.repository.IphoneRepository;
import MyIphoneShop.demo.repository.IphoneVariantRepository;
import MyIphoneShop.demo.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IphoneService {

    private final IphoneRepository iphoneRepository;
    private final CategoryRepository categoryRepository;
    private final IphoneVariantRepository iphoneVariantRepository;
    private final IphoneImageRepository iphoneImageRepository;
    private final OrderDetailRepository orderDetailRepository;

    public List<IphoneResponse> getAllActiveIphones() {
        List<Iphone> iphoneList = iphoneRepository.findByIsDeletedFalse();
        return iphoneList.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public IphoneResponse getIphoneById(Integer id) {
        Iphone iphone = iphoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));
        if (iphone.getIsDeleted())
            throw new RuntimeException("Sản phẩm này đã ngừng kinh doanh!");
        return mapToResponse(iphone);
    }

    private IphoneResponse mapToResponse(Iphone iphone) {
        IphoneResponse response = new IphoneResponse();
        response.setIphoneId(iphone.getIphoneId());
        response.setName(iphone.getName());
        response.setDescription(iphone.getDescription());

        if (iphone.getCategory() != null) {
            response.setCategoryName(iphone.getCategory().getName());
            response.setCategoryId(iphone.getCategory().getCategoryId());
        }

        List<IphoneImage> images = iphoneImageRepository.findByIphone_IphoneId(iphone.getIphoneId());
        if (!images.isEmpty()) {
            response.setImageUrls(images.stream().map(IphoneImage::getImageUrl).collect(Collectors.toList()));
        }

        if (iphone.getVariants() != null) {
            List<VariantDTO> variantDTOs = iphone.getVariants().stream()
                    .filter(v -> !v.getIsDeleted())
                    .map(v -> {
                        VariantDTO dto = new VariantDTO();
                        dto.setVariantId(v.getVariantId());
                        dto.setColor(v.getColor());
                        dto.setSize(v.getSize());
                        dto.setPrice(v.getPrice());
                        dto.setStockQuantity(v.getStockQuantity());
                        return dto;
                    }).collect(Collectors.toList());
            response.setVariants(variantDTOs);

            if (!variantDTOs.isEmpty()) {
                response.setPrice(variantDTOs.get(0).getPrice());
            } else {
                response.setPrice(BigDecimal.ZERO);
            }
        }
        return response;
    }

    private String saveImageLocally(MultipartFile file) {
        try {
            String uploadDir = "uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists())
                dir.mkdirs();

            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.copy(file.getInputStream(), path);
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu ảnh: " + e.getMessage());
        }
    }

    @Transactional
    public String createIphone(CreateIphoneRequest request, List<MultipartFile> images) {
        Iphone iphone = new Iphone();
        iphone.setName(request.getName());
        iphone.setDescription(request.getDescription());
        iphone.setIsDeleted(false);
        iphone.setPrice(request.getPrice());

        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục!"));
            iphone.setCategory(category);
        }

        Iphone savedIphone = iphoneRepository.save(iphone);

        if (images != null && !images.isEmpty()) {
            boolean isFirst = true;
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    String fileName = saveImageLocally(file);
                    IphoneImage image = new IphoneImage(null, savedIphone, fileName, isFirst);
                    isFirst = false;
                    iphoneImageRepository.save(image);
                }
            }
        }

        if (request.getVariants() != null && !request.getVariants().isEmpty()) {
            for (var vReq : request.getVariants()) {
                IphoneVariant variant = new IphoneVariant();
                variant.setIphone(savedIphone);
                variant.setColor(vReq.getColor());
                variant.setSize(vReq.getSize());
                variant.setPrice(request.getPrice());
                variant.setStockQuantity(vReq.getStockQuantity() != null ? vReq.getStockQuantity() : 0);
                variant.setIsDeleted(false);
                variant.setSku("SKU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                iphoneVariantRepository.save(variant);
            }
        }
        return "Thêm sản phẩm thành công!";
    }

    @Transactional
    public String updateIphone(Integer iphoneId, UpdateIphoneRequest request, List<MultipartFile> images) {
        Iphone iphone = iphoneRepository.findById(iphoneId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm!"));

        if (request.getName() != null)
            iphone.setName(request.getName());
        if (request.getDescription() != null)
            iphone.setDescription(request.getDescription());
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow();
            iphone.setCategory(category);
        }
        if (request.getPrice() != null) {
            iphone.setPrice(request.getPrice());
        }

        iphone = iphoneRepository.save(iphone);

        if (request.getVariants() != null && !request.getVariants().isEmpty()) {
            List<IphoneVariant> existingVariants = iphone.getVariants();

            if (existingVariants != null) {
                for (IphoneVariant v : existingVariants) {
                    v.setIsDeleted(true);
                    iphoneVariantRepository.save(v);
                }
            }

            for (var vReq : request.getVariants()) {
                IphoneVariant matchedVariant = null;

                if (existingVariants != null) {
                    for (IphoneVariant v : existingVariants) {
                        if (v.getColor().equals(vReq.getColor()) && v.getSize().equals(vReq.getSize())) {
                            matchedVariant = v;
                            break;
                        }
                    }
                }

                if (matchedVariant != null) {
                    matchedVariant.setStockQuantity(vReq.getStockQuantity());
                    matchedVariant.setPrice(request.getPrice() != null ? request.getPrice() : iphone.getPrice());
                    matchedVariant.setIsDeleted(false);
                    iphoneVariantRepository.save(matchedVariant);
                } else {
                    IphoneVariant newVariant = new IphoneVariant();
                    newVariant.setIphone(iphone);
                    newVariant.setColor(vReq.getColor());
                    newVariant.setSize(vReq.getSize());
                    newVariant.setPrice(request.getPrice() != null ? request.getPrice() : iphone.getPrice());
                    newVariant.setStockQuantity(vReq.getStockQuantity() != null ? vReq.getStockQuantity() : 0);
                    newVariant.setIsDeleted(false);
                    newVariant.setSku("SKU-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                    iphoneVariantRepository.save(newVariant);
                }
            }
        } else if (request.getPrice() != null && iphone.getVariants() != null) {
            for (IphoneVariant v : iphone.getVariants()) {
                if (!v.getIsDeleted()) {
                    v.setPrice(request.getPrice());
                    iphoneVariantRepository.save(v);
                }
            }
        }

        if (images != null && !images.isEmpty()) {
            List<IphoneImage> oldImages = iphoneImageRepository.findByIphone_IphoneId(iphoneId);
            iphoneImageRepository.deleteAll(oldImages);

            boolean isFirst = true;
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    String fileName = saveImageLocally(file);
                    IphoneImage image = new IphoneImage(null, iphone, fileName, isFirst);
                    isFirst = false;
                    iphoneImageRepository.save(image);
                }
            }
        }
        return "Cập nhật thành công!";
    }

    @Transactional
    public String deleteIphone(Integer id) {
        Iphone iphone = iphoneRepository.findById(id).orElseThrow();
        iphone.setIsDeleted(true);
        if (iphone.getVariants() != null) {
            iphone.getVariants().forEach(v -> {
                v.setIsDeleted(true);
                iphoneVariantRepository.save(v);
            });
        }
        iphoneRepository.save(iphone);
        return "Đã xóa sản phẩm!";
    }

    public List<IphoneResponse> searchAndFilterIphones(String keyword, Integer categoryId, BigDecimal minPrice,
            BigDecimal maxPrice, List<Color> colors, List<Size> sizes) {
        return iphoneRepository.findByIsDeletedFalse().stream()
                .filter(c -> {
                    if (keyword != null && !c.getName().toLowerCase().contains(keyword.toLowerCase()))
                        return false;
                    if (categoryId != null
                            && (c.getCategory() == null || !c.getCategory().getCategoryId().equals(categoryId)))
                        return false;
                    return c.getVariants().stream().anyMatch(v -> {
                        if (v.getIsDeleted())
                            return false;
                        if (minPrice != null && v.getPrice().compareTo(minPrice) < 0)
                            return false;
                        if (maxPrice != null && v.getPrice().compareTo(maxPrice) > 0)
                            return false;
                        if (colors != null && !colors.isEmpty() && !colors.contains(v.getColor()))
                            return false;
                        if (sizes != null && !sizes.isEmpty() && !sizes.contains(v.getSize()))
                            return false;
                        return true;
                    });
                })
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách sản phẩm bán chạy nhất (Top N)
     * Truy vấn bảng OrderDetail, gom nhóm theo iPhone ID, tính tổng số lượng bán ra
     */
    public List<BestSellerResponse> getBestSellers(int limit) {
        List<Object[]> topSelling = orderDetailRepository.findTopSellingIphoneIds(PageRequest.of(0, limit));
        List<BestSellerResponse> result = new ArrayList<>();

        for (Object[] row : topSelling) {
            Integer iphoneId = (Integer) row[0];
            Long totalSold = (Long) row[1];

            Iphone iphone = iphoneRepository.findById(iphoneId).orElse(null);
            if (iphone == null || iphone.getIsDeleted()) continue;

            BestSellerResponse dto = new BestSellerResponse();
            dto.setIphoneId(iphone.getIphoneId());
            dto.setName(iphone.getName());
            dto.setDescription(iphone.getDescription());
            dto.setTotalSold(totalSold);

            if (iphone.getCategory() != null) {
                dto.setCategoryName(iphone.getCategory().getName());
            }

            // Lấy ảnh
            List<IphoneImage> images = iphoneImageRepository.findByIphone_IphoneId(iphoneId);
            if (!images.isEmpty()) {
                dto.setImageUrls(images.stream().map(IphoneImage::getImageUrl).collect(Collectors.toList()));
            }

            // Lấy giá thấp nhất từ các variant đang còn bán
            if (iphone.getVariants() != null) {
                iphone.getVariants().stream()
                        .filter(v -> !v.getIsDeleted())
                        .map(IphoneVariant::getPrice)
                        .min(BigDecimal::compareTo)
                        .ifPresent(dto::setPrice);
            }

            result.add(dto);
        }

        return result;
    }
}