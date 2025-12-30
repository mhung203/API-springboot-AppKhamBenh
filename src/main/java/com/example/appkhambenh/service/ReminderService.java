package com.example.appkhambenh.service;

import com.example.appkhambenh.entity.LichHen;
import com.example.appkhambenh.entity.ThongBao;
import com.example.appkhambenh.repository.LichHenRepository;
import com.example.appkhambenh.repository.ThongBaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderService {

    @Autowired
    private LichHenRepository lichHenRepository;

    @Autowired
    private ThongBaoRepository thongBaoRepository;

    @Scheduled(cron = "0 0 6 * * ?")
    //@Scheduled(fixedRate = 10000)
    public void generateReminders() {
        System.out.println(">>> [BOT] Bắt đầu quét lịch khám để nhắc nhở...");


        LocalDate targetDate = LocalDate.now().plusDays(1);


        List<LichHen> listBooking = lichHenRepository.findByNgayAndTrangThai(targetDate, "Đã xác nhận");

        if (listBooking.isEmpty()) {
            System.out.println(">>> [BOT] Không có lịch hẹn nào cần nhắc cho ngày " + targetDate);
            return;
        }
        for (LichHen lichHen : listBooking) {
            try {
                ThongBao thongBao = new ThongBao();
                String hoTenBacSi = lichHen.getBacSi().getUser().getFirstname() + " " + lichHen.getBacSi().getUser().getLastname();
                thongBao.setUserId(lichHen.getBenhNhan().getUserId());
                thongBao.setTieuDe("Nhắc nhở lịch khám sắp tới");
                thongBao.setNoiDung("Bạn có lịch khám với BS " + hoTenBacSi +
                        " vào ngày kia (" + targetDate + "). Vui lòng đến đúng giờ!");

                thongBao.setThoiGian(LocalDateTime.now());
                thongBao.setDaXem(false);

                thongBaoRepository.save(thongBao);

                System.out.println(">>> [BOT] Đã tạo nhắc nhở cho Bệnh nhân ID: " + lichHen.getBenhNhan().getUserId());
            } catch (Exception e) {
                System.err.println(">>> [BOT] Lỗi khi tạo thông báo: " + e.getMessage());
            }
        }
    }
}