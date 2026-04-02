package MyIphoneShop.demo.controller;

import MyIphoneShop.demo.dto.ChartDTO;
import MyIphoneShop.demo.dto.DashboardResponse;
import MyIphoneShop.demo.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboardStats() {
        return ResponseEntity.ok(dashboardService.getGeneralStats());
    }
    @GetMapping("/chart/daily")
    public ResponseEntity<List<ChartDTO>> getDailyChart(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        return ResponseEntity.ok(dashboardService.getDailyRevenueChart(year, month));
    }

    @GetMapping("/chart/monthly")
    public ResponseEntity<List<ChartDTO>> getMonthlyChart(
            @RequestParam(required = false) Integer year) {
        return ResponseEntity.ok(dashboardService.getMonthlyRevenueChart(year));
    }

    @GetMapping("/chart/yearly")
    public ResponseEntity<List<ChartDTO>> getYearlyChart() {
        return ResponseEntity.ok(dashboardService.getYearlyRevenueChart());
    }
}