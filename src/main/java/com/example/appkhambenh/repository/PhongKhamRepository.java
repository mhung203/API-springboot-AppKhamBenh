package com.example.appkhambenh.repository;

import com.example.appkhambenh.entity.PhongKham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhongKhamRepository extends JpaRepository<PhongKham, Integer> {

}
