package com.example.appkhambenh.controller;

import com.example.appkhambenh.entity.Khoa;
import com.example.appkhambenh.repository.KhoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khoa")
@CrossOrigin(origins = "*")
public class KhoaController {

    @Autowired
    private KhoaRepository khoaRepository;

    @GetMapping("/search")
    public List<Khoa> searchKhoa(@RequestParam String name) {
        return khoaRepository.findByTenKhoaContaining(name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Khoa> getKhoaById(@PathVariable Integer id) {
        return khoaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Khoa> updateKhoa(@PathVariable Integer id, @RequestBody Khoa khoaDetails) {
        return khoaRepository.findById(id).map(khoa -> {
            khoa.setTenKhoa(khoaDetails.getTenKhoa());
            khoa.setMoTa(khoaDetails.getMoTa());
            return ResponseEntity.ok(khoaRepository.save(khoa));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteKhoa(@PathVariable Integer id) {
        return khoaRepository.findById(id).map(khoa -> {
            khoaRepository.delete(khoa);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping
    public Khoa createKhoa(@RequestBody Khoa khoa) {
        return khoaRepository.save(khoa);
    }
    @GetMapping
    public List<Khoa> getAllKhoa() {
        return khoaRepository.findAll();
    }
}