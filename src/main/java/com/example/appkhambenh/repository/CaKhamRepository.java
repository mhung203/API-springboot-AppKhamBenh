package com.example.appkhambenh.repository;

import com.example.appkhambenh.entity.CaKham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CaKhamRepository extends JpaRepository<CaKham, Integer> {


    List<CaKham> findByBacSi_BacSiIdAndNgay(Integer bacSiId, LocalDate ngay);
}