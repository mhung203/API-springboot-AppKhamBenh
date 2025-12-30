package com.example.appkhambenh.controller;
import com.example.appkhambenh.entity.*;
import com.example.appkhambenh.repository.*;
import com.example.appkhambenh.dto.BacSiDTO;
import com.example.appkhambenh.service.CloudinaryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/bac-si")
@CrossOrigin(origins = "*")
public class BacSiController {
    @Autowired
    private BacSiRepository bacSiRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private KhoaRepository khoaRepository;
    @Autowired
    private PhongKhamRepository phongKhamRepository;
    @Autowired
    private LichHenRepository lichHenRepository;
    @GetMapping
    public List<BacSi> getAllBacSi()
    {
        return bacSiRepository.findAll();
    }
    @GetMapping("/{id}")
    public ResponseEntity<BacSi> getBacSiId(@PathVariable Integer id)
    {
        return bacSiRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/khoa/{khoaId}")
    public List<BacSi> getBacSiByKhoa(@PathVariable Integer khoaId)
    {
        return bacSiRepository.findByKhoa_KhoaId(khoaId);
    }
    @PostMapping
    public ResponseEntity<?> createBacSi(@RequestBody BacSiDTO request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy User ID: " + request.getUserId()));
        Khoa khoa = khoaRepository.findById(request.getKhoaId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Khoa ID: " + request.getKhoaId()));
        if (bacSiRepository.findByUser_UserId(request.getUserId()).isPresent()) {
            return ResponseEntity.badRequest().body("User này đã là bác sĩ rồi!");
        }
        BacSi newBacSi = new BacSi();
        newBacSi.setUser(user);
        newBacSi.setKhoa(khoa);
        newBacSi.setBangCap(request.getBangCap());
        newBacSi.setKinhNghiem(request.getKinhNghiem());
        return ResponseEntity.ok(bacSiRepository.save(newBacSi));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBacSi(@PathVariable Integer id, @RequestBody BacSiDTO request) {
        return bacSiRepository.findById(id).map(bacSi -> {
            if(request.getBangCap() != null) {
                bacSi.setBangCap(request.getBangCap());
            }
            if (request.getKinhNghiem() != null) {
                bacSi.setKinhNghiem(request.getKinhNghiem());
            }
            if(request.getKhoaId() != null) {
                Khoa khoa = khoaRepository.findById(request.getKhoaId())
                        .orElseThrow(() -> new RuntimeException("Khoa không tồn tại"));
                bacSi.setKhoa(khoa);
            }
            if(request.getGiaKham() != null){
                bacSi.setGiaKham(request.getGiaKham());
            }
            if (request.getPhongId() != null) {
                PhongKham phong = phongKhamRepository.findById(request.getPhongId())
                        .orElseThrow(() -> new RuntimeException("Phòng khám không tồn tại ID: " + request.getPhongId()));
                bacSi.setPhongKham(phong);
            }

            return ResponseEntity.ok(bacSiRepository.save(bacSi));
        }).orElse(ResponseEntity.notFound().build());
    }
    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAnhBacSi(@PathVariable Integer id, @RequestParam("file") MultipartFile file) {
        try {
            BacSi bacSi = bacSiRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Bác sĩ không tồn tại"));

            String url = cloudinaryService.uploadImage(file);

            bacSi.setAnhDaiDien(url);
            bacSiRepository.save(bacSi);

            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi: " + e.getMessage());
        }
    }
    @GetMapping("/lich-kham")
    public ResponseEntity<?> getLichKham(@RequestParam Integer bacSiId,
                                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<LichHen> listLichHen = lichHenRepository.findLichLamViecBacSi(bacSiId, date);
        return ResponseEntity.ok(listLichHen);
    }
    @GetMapping("/lich-hen/{id}")
    public ResponseEntity<?> getChiTietLichHen(@PathVariable Integer id) {
        return ResponseEntity.ok(lichHenRepository.findById(id).orElse(null));
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<BacSi> getBacSiByUserId(@PathVariable Integer userId) {
        return bacSiRepository.findByUser_UserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
