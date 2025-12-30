package com.example.appkhambenh.repository;

import com.example.appkhambenh.entity.TinTuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TinTucRepository extends JpaRepository<TinTuc, Integer> {

    boolean existsByLinkBaiViet(String linkBaiViet);
}