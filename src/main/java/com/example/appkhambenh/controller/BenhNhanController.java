package com.example.appkhambenh.controller;

import com.example.appkhambenh.dto.BenhNhanDTO;
import com.example.appkhambenh.entity.BenhNhan;
import com.example.appkhambenh.entity.User;
import com.example.appkhambenh.repository.BenhNhanRepository;
import com.example.appkhambenh.repository.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/benh-nhan")
@CrossOrigin(origins = "*")
public class BenhNhanController {

    @Autowired
    private BenhNhanRepository benhNhanRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getBenhNhanById(@PathVariable Integer userId) {
        return benhNhanRepository.findById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createBenhNhan(@RequestBody BenhNhanDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy User ID: " + request.getUserId()));
        if (benhNhanRepository.existsById(request.getUserId())) {
            return ResponseEntity.badRequest().body("User này đã có hồ sơ bệnh nhân rồi!");
        }
        BenhNhan benhNhan = new BenhNhan();
        benhNhan.setUser(user);
        benhNhan.setMedicalHistory(request.getMedicalHistory());
        String hoTenDayDu = "";
        if (user.getFirstname() != null) hoTenDayDu += user.getFirstname();
        if (user.getLastname() != null) hoTenDayDu += " " + user.getLastname();
        benhNhan.setHoTen(hoTenDayDu.trim());
        benhNhan.setSoDienThoai(user.getPhone());
        benhNhan.setGioiTinh(user.getGender());
        return ResponseEntity.ok(benhNhanRepository.save(benhNhan));
    }
}