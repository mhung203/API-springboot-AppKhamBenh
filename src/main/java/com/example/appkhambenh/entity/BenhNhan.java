package com.example.appkhambenh.entity;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "BenhNhan")
@Data
public class BenhNhan {

    @Id
    @Column(name = "userId")
    private Integer userId;

    @Column(name = "medicalHistory", columnDefinition = "TEXT")
    private String medicalHistory;
    @Column(name = "ho_ten")
    private String hoTen;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "gioi_tinh")
    private String gioiTinh;

    @OneToOne
    @MapsId
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user;
}