package MyIphoneShop.demo.service;

import MyIphoneShop.demo.dto.ChartDTO;
import MyIphoneShop.demo.dto.DashboardResponse;
import MyIphoneShop.demo.entity.enums.OrderStatus;
import MyIphoneShop.demo.repository.IphoneRepository;
import MyIphoneShop.demo.repository.OrderRepository;
import MyIphoneShop.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final OrderRepository orderRepository;
    private final IphoneRepository iphoneRepository;
    private final UserRepository userRepository;

    public DashboardResponse getGeneralStats() {
        DashboardResponse stats = new DashboardResponse();

        BigDecimal revenue = orderRepository.sumTotalRevenue();
        stats.setTotalRevenue(revenue != null ? revenue : BigDecimal.ZERO);

        stats.setTotalOrders(orderRepository.count());
        stats.setPendingOrders(orderRepository.countByStatus(OrderStatus.PENDING));
        stats.setTotalProducts(iphoneRepository.countByIsDeletedFalse());
        stats.setTotalCustomers((int) userRepository.countCustomers());

        return stats;
    }

    private List<ChartDTO> mapToChartDTO(List<Object[]> results) {
        List<ChartDTO> chartData = new ArrayList<>();
        for (Object[] row : results) {
            String label = row[0].toString();
            BigDecimal value = new BigDecimal(row[1].toString());
            chartData.add(new ChartDTO(label, value));
        }
        return chartData;
    }

    // ĐÃ FIX: Ép nhận tham số Year và Month
    public List<ChartDTO> getDailyRevenueChart(Integer year, Integer month) {
        int targetYear = (year != null) ? year : LocalDate.now().getYear();
        int targetMonth = (month != null) ? month : LocalDate.now().getMonthValue();

        return mapToChartDTO(orderRepository.getRevenueByDay(targetYear, targetMonth));
    }

    // ĐÃ FIX: Ép nhận tham số Year
    public List<ChartDTO> getMonthlyRevenueChart(Integer year) {
        int targetYear = (year != null) ? year : LocalDate.now().getYear();

        return mapToChartDTO(orderRepository.getRevenueByMonth(targetYear));
    }

    // Biểu đồ năm thì lấy chung các năm, không cần tham số
    public List<ChartDTO> getYearlyRevenueChart() {
        return mapToChartDTO(orderRepository.getRevenueByYear());
    }
}