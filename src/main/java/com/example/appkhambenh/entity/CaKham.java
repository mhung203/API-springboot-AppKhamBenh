package com.example.appkhambenh.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "CaKham")
@Data
public class CaKham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String tenCa;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private LocalDate ngay;

    @ManyToOne
    @JoinColumn(name = "bacSiId")
    private BacSi bacSi;
    private String trangThai;
}