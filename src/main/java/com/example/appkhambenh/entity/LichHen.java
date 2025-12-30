package com.example.appkhambenh.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Entity
// THÊM DÒNG uniqueConstraints NÀY:
@Table(name = "LichHen", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"idBacSi", "ngay", "timeStart"})
})
@Data
public class LichHen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lichHenId")
    private Integer lichHenId;

    // Liên kết với Bệnh Nhân
    @ManyToOne
    @JoinColumn(name = "idBenhNhan")
    private BenhNhan benhNhan;

    @ManyToOne
    @JoinColumn(name = "idBacSi")
    private BacSi bacSi;

    @Column(name = "idPhong")
    private Integer idPhong;

    @Column(name = "idCa")
    private Integer idCa;

    @Column(name = "ngay")
    private LocalDate ngay;

    @Column(name = "timeStart")
    private LocalTime timeStart;

    @Column(name = "timeEnd")
    private LocalTime timeEnd;

    @Column(name = "CHIPHI")
    private Double chiPhi;

    @Column(name = "LYDOKHAM", columnDefinition = "TEXT")
    private String lyDoKham;

    @Column(name = "NGAYHEN")
    private LocalDate ngayHen;

    @Column(name = "TRANGTHAI")
    private String trangThai;
}