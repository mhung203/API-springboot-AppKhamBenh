package com.example.appkhambenh.controller;

import com.example.appkhambenh.entity.PhongKham;
import com.example.appkhambenh.repository.PhongKhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phong-kham")
@CrossOrigin(origins = "*")
public class PhongKhamController {
    @Autowired
    private PhongKhamRepository phongKhamRepository;


    @PostMapping
    public PhongKham createPhong(@RequestBody PhongKham phong) {
        return phongKhamRepository.save(phong);
    }


    @GetMapping
    public List<PhongKham> getAllPhong() {
        return phongKhamRepository.findAll();
    }
}