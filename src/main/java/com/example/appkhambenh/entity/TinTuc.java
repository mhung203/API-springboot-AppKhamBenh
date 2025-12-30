package com.example.appkhambenh.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tin_tuc")
public class TinTuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tieuDe;

    @Column(columnDefinition = "TEXT")
    private String hinhAnh;

    @Column(columnDefinition = "TEXT")
    private String moTaNgan;

    @Column(unique = true)
    private String linkBaiViet;

    private LocalDateTime ngayDang;
    public TinTuc() {}
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }
    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }
    public String getMoTaNgan() { return moTaNgan; }
    public void setMoTaNgan(String moTaNgan) { this.moTaNgan = moTaNgan; }
    public String getLinkBaiViet() { return linkBaiViet; }
    public void setLinkBaiViet(String linkBaiViet) { this.linkBaiViet = linkBaiViet; }
    public LocalDateTime getNgayDang() { return ngayDang; }
    public void setNgayDang(LocalDateTime ngayDang) { this.ngayDang = ngayDang; }
}