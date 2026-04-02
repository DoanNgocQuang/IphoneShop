package MyIphoneShop.demo.repository;

import MyIphoneShop.demo.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    // Lấy toàn bộ sổ địa chỉ của một khách hàng
    List<Address> findByUser_UserId(Integer userId);
}