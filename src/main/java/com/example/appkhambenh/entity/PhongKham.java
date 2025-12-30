package com.example.appkhambenh.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "PhongKham")
@Data
public class PhongKham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ten_phong")
    private String tenPhong;

    @Column(name = "vi_tri")
    private String viTri;

    @Column(name = "mo_ta")
    private String moTa;
}