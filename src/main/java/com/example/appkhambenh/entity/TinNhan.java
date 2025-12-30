package com.example.appkhambenh.entity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TinNhan")
public class TinNhan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer nguoiGuiId;
    private Integer nguoiNhanId;
    private String noiDung;
    private LocalDateTime thoiGian;
    public TinNhan() {}

    public TinNhan(Integer nguoiGuiId, Integer nguoiNhanId, String noiDung) {
        this.nguoiGuiId = nguoiGuiId;
        this.nguoiNhanId = nguoiNhanId;
        this.noiDung = noiDung;
        this.thoiGian = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNguoiGuiId() {
        return nguoiGuiId;
    }

    public void setNguoiGuiId(Integer nguoiGuiId) {
        this.nguoiGuiId = nguoiGuiId;
    }

    public Integer getNguoiNhanId() {
        return nguoiNhanId;
    }

    public void setNguoiNhanId(Integer nguoiNhanId) {
        this.nguoiNhanId = nguoiNhanId;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public LocalDateTime getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }
}
