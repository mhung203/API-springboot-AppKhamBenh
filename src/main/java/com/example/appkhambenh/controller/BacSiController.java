package com.example.appkhambenh.controller;
import com.example.appkhambenh.entity.BacSi;
import com.example.appkhambenh.entity.Khoa;
import com.example.appkhambenh.entity.User;
import com.example.appkhambenh.repository.BacSiRepository;
import com.example.appkhambenh.repository.KhoaRepository;
import com.example.appkhambenh.repository.UserRepository;
import com.example.appkhambenh.dto.BacSiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    private KhoaRepository khoaRepository;
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
            bacSi.setBangCap(request.getBangCap());
            bacSi.setKinhNghiem(request.getKinhNghiem());

            if(request.getKhoaId() != null) {
                Khoa khoa = khoaRepository.findById(request.getKhoaId())
                        .orElseThrow(() -> new RuntimeException("Khoa không tồn tại"));
                bacSi.setKhoa(khoa);
            }
            return ResponseEntity.ok(bacSiRepository.save(bacSi));
        }).orElse(ResponseEntity.notFound().build());
    }
}
