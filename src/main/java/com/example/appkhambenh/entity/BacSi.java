package com.example.appkhambenh.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
@Entity
@Table(name="BacSi")
@Data
public class BacSi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bacSiId")
    private Integer bacSiId;
    @Column(name="bangCap")
    private String bangCap;
    @Column(name="kinhNghiem")
    private Integer kinhNghiem;
    @OneToOne
    @JoinColumn(name="userId", unique = true)
    private User user;
    @ManyToOne
    @JoinColumn(name="khoaId")
    private Khoa khoa;
    @Column(name = "anhDaiDien")
    private String anhDaiDien;
    @Column(name = "gia_kham")
    private Double giaKham;
    @ManyToOne
    @JoinColumn(name = "phong_id")
    @JsonIgnoreProperties({"danhSachBacSi", "hibernateLazyInitializer", "handler"})
    private PhongKham phongKham;
}
