package com.example.appkhambenh.entity;
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
}
