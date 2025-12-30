package com.example.appkhambenh.controller;
import com.example.appkhambenh.entity.BacSi;
import com.example.appkhambenh.entity.CaKham;
import com.example.appkhambenh.repository.BacSiRepository;
import com.example.appkhambenh.repository.CaKhamRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/ca-kham")
@CrossOrigin(origins = "*")
public class CaKhamController {

    @Autowired
    private CaKhamRepository caKhamRepository;

    @Autowired
    private BacSiRepository bacSiRepository;

    @PostMapping
    public ResponseEntity<?> createCaKham(@RequestBody CaKhamDTO request) {
        BacSi bacSi = bacSiRepository.findById(request.getBacSiId())
                .orElseThrow(() -> new RuntimeException("Bác sĩ không tồn tại"));

        CaKham ca = new CaKham();
        ca.setBacSi(bacSi);
        ca.setTenCa(request.getTenCa());
        ca.setNgay(request.getNgay());
        ca.setTimeStart(request.getTimeStart());
        ca.setTimeEnd(request.getTimeEnd());
        ca.setTrangThai("Còn trống");

        return ResponseEntity.ok(caKhamRepository.save(ca));
    }

    @PostMapping("/auto-generate")
    public ResponseEntity<?> autoGenerateCa(@RequestParam Integer bacSiId,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngay) {
        BacSi bacSi = bacSiRepository.findById(bacSiId)
                .orElseThrow(() -> new RuntimeException("Bác sĩ không tồn tại"));

        CaKham sang = new CaKham();
        sang.setBacSi(bacSi); sang.setNgay(ngay); sang.setTenCa("Sáng");
        sang.setTimeStart(LocalTime.of(7, 0)); sang.setTimeEnd(LocalTime.of(11, 0));
        sang.setTrangThai("Còn trống");
        caKhamRepository.save(sang);


        CaKham chieu = new CaKham();
        chieu.setBacSi(bacSi); chieu.setNgay(ngay); chieu.setTenCa("Chiều");
        chieu.setTimeStart(LocalTime.of(13, 0)); chieu.setTimeEnd(LocalTime.of(17, 0));
        chieu.setTrangThai("Còn trống");
        caKhamRepository.save(chieu);


        CaKham toi = new CaKham();
        toi.setBacSi(bacSi); toi.setNgay(ngay); toi.setTenCa("Tối");
        toi.setTimeStart(LocalTime.of(18, 0)); toi.setTimeEnd(LocalTime.of(21, 0));
        toi.setTrangThai("Còn trống");
        caKhamRepository.save(toi);

        return ResponseEntity.ok("Đã tạo xong 3 ca cho bác sĩ!");
    }

    @GetMapping
    public List<CaKham> getCaKhamByBacSiAndNgay(
            @RequestParam Integer bacSiId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngay) {

        return caKhamRepository.findByBacSi_BacSiIdAndNgay(bacSiId, ngay);
    }

    @Data
    static
    class CaKhamDTO {
        private Integer bacSiId;
        private String tenCa;
        private LocalDate ngay;
        private LocalTime timeStart;
        private LocalTime timeEnd;
    }
}

