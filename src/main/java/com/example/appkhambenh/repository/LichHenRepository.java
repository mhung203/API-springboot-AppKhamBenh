package com.example.appkhambenh.repository;

import com.example.appkhambenh.entity.LichHen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface LichHenRepository extends JpaRepository<LichHen, Integer> {
    // Tìm lịch hẹn theo Bệnh nhân (Để hiển thị lịch sử khám)
    List<LichHen> findByBenhNhan_UserId(Integer benhNhanId);

    // Tìm lịch hẹn theo Bác sĩ (Để bác sĩ xem lịch làm việc)
    List<LichHen> findByBacSi_BacSiId(Integer bacSiId);
    boolean existsByBacSi_BacSiIdAndNgayAndTimeStart(Integer bacSiId, LocalDate ngay, LocalTime timeStart);
    @Query("SELECT l FROM LichHen l WHERE l.bacSi.id = :bacSiId AND l.ngay = :date")
    List<LichHen> findLichLamViecBacSi(@Param("bacSiId") Integer bacSiId,
                                       @Param("date") LocalDate date);
    List<LichHen> findByNgayAndTrangThai(LocalDate ngay, String trangThai);
    List<LichHen> findByNgay(LocalDate ngay);
    List<LichHen> findByNgayOrderByTimeStartAsc(LocalDate ngay);
}