package com.example.appkhambenh.controller;

import com.example.appkhambenh.entity.ThongBao;
import com.example.appkhambenh.repository.ThongBaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class ThongBaoController {

    @Autowired
    private ThongBaoRepository thongBaoRepository;


    @GetMapping("/{userId}")
    public List<ThongBao> getNotifications(@PathVariable Integer userId) {
        return thongBaoRepository.findByUserIdOrderByThoiGianDesc(userId);
    }


    @PutMapping("/read/{id}")
    public void markAsRead(@PathVariable Integer id) {
        ThongBao tb = thongBaoRepository.findById(id).orElse(null);
        if (tb != null) {
            tb.setDaXem(true);
            thongBaoRepository.save(tb);
        }
    }
}