package com.example.appkhambenh.controller;

import com.example.appkhambenh.dto.LichHenDTO;
import com.example.appkhambenh.entity.BacSi;
import com.example.appkhambenh.entity.BenhNhan;
import com.example.appkhambenh.entity.LichHen;
import com.example.appkhambenh.entity.ThongBao;
import com.example.appkhambenh.repository.BacSiRepository;
import com.example.appkhambenh.repository.BenhNhanRepository;
import com.example.appkhambenh.repository.LichHenRepository;
import com.example.appkhambenh.repository.ThongBaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/lich-hen")
@CrossOrigin(origins = "*")
public class LichHenController {

    @Autowired
    private LichHenRepository lichHenRepository;
    @Autowired
    private BenhNhanRepository benhNhanRepository;
    @Autowired
    private BacSiRepository bacSiRepository;
    @Autowired
    private ThongBaoRepository thongBaoRepository;
    @PostMapping
    public ResponseEntity<?> datLichKham(@RequestBody LichHenDTO request) {
        try {
            boolean daTonTai = lichHenRepository.existsByBacSi_BacSiIdAndNgayAndTimeStart(
                    request.getBacSiId(), request.getNgayKham(), request.getTimeStart());

            if (daTonTai) {
                return ResponseEntity.badRequest().body("Tiếc quá! Ca khám này đã có người đặt rồi.");
            }
            BenhNhan benhNhan = benhNhanRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy bệnh nhân"));
            BacSi bacSi = bacSiRepository.findById(request.getBacSiId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ"));

            LichHen lichHen = new LichHen();
            lichHen.setBenhNhan(benhNhan);
            lichHen.setBacSi(bacSi);
            if (bacSi.getPhongKham() != null) {
                lichHen.setIdPhong(bacSi.getPhongKham().getId());
            } else {
                lichHen.setIdPhong(null);
            }

            lichHen.setIdCa(request.getIdCa());
            lichHen.setNgay(request.getNgayKham());
            lichHen.setTimeStart(request.getTimeStart());
            lichHen.setTimeEnd(request.getTimeEnd());
            Double giaKhamBacSi = bacSi.getGiaKham();
            if(giaKhamBacSi == null) {
                giaKhamBacSi = 100000.0;
            }
            lichHen.setChiPhi(giaKhamBacSi);

            lichHen.setLyDoKham(request.getLyDoKham());
            lichHen.setNgayHen(LocalDate.now());
            lichHen.setTrangThai("Chưa xác nhận");

            return ResponseEntity.ok(lichHenRepository.save(lichHen));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Rất tiếc, ca khám này đã có người đặt !");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Lỗi đặt lịch: " + e.getMessage());
        }
    }

    @PutMapping("/thanh-toan/{id}")
    public ResponseEntity<?> xacNhanThanhToan(@PathVariable Integer id) {
        return lichHenRepository.findById(id).map(lichHen -> {
            lichHen.setTrangThai("Đã xác nhận");
            LichHen savedLichHen = lichHenRepository.save(lichHen);
            try {
                ThongBao thongBao = new ThongBao();
                thongBao.setUserId(savedLichHen.getBenhNhan().getUserId());
                thongBao.setTieuDe("Lịch khám đã được xác nhận!");
                thongBao.setNoiDung("Lịch khám ngày " + savedLichHen.getNgay() + " của bạn đã được xác nhận. Vui lòng đến đúng giờ.");
                thongBao.setThoiGian(java.time.LocalDateTime.now());
                thongBao.setDaXem(false);

                thongBaoRepository.save(thongBao);
            } catch (Exception e) {
                System.err.println("Lỗi tạo thông báo: " + e.getMessage());
            }
            return ResponseEntity.ok(lichHenRepository.save(lichHen));
        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/benh-nhan/{userId}")
    public List<LichHen> getLichSuKham(@PathVariable Integer userId) {
        return lichHenRepository.findByBenhNhan_UserId(userId);
    }
    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllLichHenByDate(@RequestParam(required = false) String date) {
        if (date == null) {
            return ResponseEntity.ok(lichHenRepository.findAll());
        } else {
            LocalDate localDate = LocalDate.parse(date);
            return ResponseEntity.ok(lichHenRepository.findByNgayOrderByTimeStartAsc(localDate));
        }
    }


    @PutMapping("/huy/{id}")
    public ResponseEntity<?> huyLichHen(@PathVariable Integer id) {
        return lichHenRepository.findById(id).map(lichHen -> {


            lichHen.setTrangThai("Đã hủy");
            LichHen savedLichHen = lichHenRepository.save(lichHen);


            try {
                String hoTenBacSi = savedLichHen.getBacSi().getUser().getFirstname() + " "
                        + savedLichHen.getBacSi().getUser().getLastname();
                ThongBao tb = new ThongBao();
                tb.setUserId(savedLichHen.getBenhNhan().getUserId());
                tb.setTieuDe("Lịch khám đã bị hủy!");
                tb.setNoiDung("Rất tiếc, lịch khám ngày " + savedLichHen.getNgay() +
                        " với BS " + hoTenBacSi +
                        " đã bị hủy do bác sĩ bận đột xuất.");
                tb.setThoiGian(java.time.LocalDateTime.now());
                tb.setDaXem(false);

                thongBaoRepository.save(tb);
            } catch (Exception e) {
                System.err.println("Lỗi tạo thông báo hủy: " + e.getMessage());
            }

            return ResponseEntity.ok(savedLichHen);

        }).orElse(ResponseEntity.notFound().build());
    }

}