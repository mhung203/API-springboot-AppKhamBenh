package com.example.appkhambenh.repository;
import com.example.appkhambenh.entity.BacSi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface  BacSiRepository extends JpaRepository<BacSi,Integer> {
    List<BacSi> findByKhoa_KhoaId(Integer khoaId);
    Optional<BacSi> findByUser_UserId(Integer userId);

}
