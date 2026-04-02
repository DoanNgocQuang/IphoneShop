package MyIphoneShop.demo.controller;

import MyIphoneShop.demo.dto.CreateIphoneRequest;
import MyIphoneShop.demo.entity.enums.Color;
import MyIphoneShop.demo.entity.enums.Size;
import MyIphoneShop.demo.dto.IphoneResponse;
import MyIphoneShop.demo.service.IphoneService;
import MyIphoneShop.demo.dto.UpdateIphoneRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/iphones")
@RequiredArgsConstructor
public class IphoneController {

    private final IphoneService iphoneService;

    @GetMapping
    public ResponseEntity<List<IphoneResponse>> getAllIphones() {
        return ResponseEntity.ok(iphoneService.getAllActiveIphones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIphoneById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(iphoneService.getIphoneById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search-and-filter")
    public ResponseEntity<List<IphoneResponse>> searchAndFilterIphones(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) List<Color> colors,
            @RequestParam(required = false) List<Size> sizes
    ) {
        return ResponseEntity.ok(iphoneService.searchAndFilterIphones(keyword, categoryId, minPrice, maxPrice, colors, sizes));
    }

    @PostMapping(value = "/admin/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createIphone(
            @RequestPart("data") String dataJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            CreateIphoneRequest request = mapper.readValue(dataJson, CreateIphoneRequest.class);

            return ResponseEntity.ok(iphoneService.createIphone(request, images));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi xử lý dữ liệu: " + e.getMessage());
        }
    }

    @PutMapping(value = "/admin/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateIphone(
            @PathVariable Integer id,
            @RequestPart("data") String dataJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            UpdateIphoneRequest request = mapper.readValue(dataJson, UpdateIphoneRequest.class);

            return ResponseEntity.ok(iphoneService.updateIphone(id, request, images));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Lỗi xử lý dữ liệu: " + e.getMessage());
        }
    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<?> deleteIphone(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(iphoneService.deleteIphone(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}