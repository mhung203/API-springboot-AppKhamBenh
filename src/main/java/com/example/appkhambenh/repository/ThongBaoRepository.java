package com.example.appkhambenh.repository;

import com.example.appkhambenh.entity.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ThongBaoRepository extends JpaRepository<ThongBao, Integer> {

    // Lấy danh sách thông báo của 1 user, sắp xếp mới nhất lên đầu
    List<ThongBao> findByUserIdOrderByThoiGianDesc(Integer userId);

    // Đếm số thông báo chưa đọc (để hiện chấm đỏ)
    long countByUserIdAndDaXemFalse(Integer userId);
}