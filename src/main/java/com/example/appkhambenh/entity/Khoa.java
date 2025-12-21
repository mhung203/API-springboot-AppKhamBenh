package com.example.appkhambenh.entity;
import jakarta.persistence.*;
import jakarta.websocket.Encoder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Entity
@Table(name ="Khoa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Khoa
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="khoaId")
    private Integer khoaId;
    @Column(name = "tenKhoa", nullable = false,length = 100)
    private String tenKhoa;
    @Column(name = "moTa", columnDefinition = "TEXT")
    private String moTa;
}
