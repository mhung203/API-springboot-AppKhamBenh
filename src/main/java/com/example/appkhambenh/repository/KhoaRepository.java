package com.example.appkhambenh.repository;
import com.example.appkhambenh.entity.Khoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhoaRepository extends JpaRepository<Khoa,Integer> {
    List<Khoa> findByTenKhoaContaining(String tenKhoa);
}
