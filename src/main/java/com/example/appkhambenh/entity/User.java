package com.example.appkhambenh.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
@Entity
@Table(name = "User")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int userId;
    @Column(name = "email",nullable = false,unique = true,length = 100)
    private String email;
    @Column(name = "firstname", length = 100)
    private String firstname;
    @Column(name="lastname",length = 100)
    private String lastname;
    @Column (name = "password",nullable = false,length = 255)
    private String password;
    @Column (name = "birthday")
    private LocalDate birthday;
    @Column(name = "gender",length = 10)
    private String gender;
    @Column (name = "phone",length = 20)
    private String phone;
    @Column(name = "otp_code",length = 10)
    private String otpCode;
    @Column(name = "otp_expiry")
    private LocalDateTime otpExpiry;
    @Column(name="is_active",columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean isActive;
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    @Column (name = "address", length = 255)
    private String address;
    @Column (name="role", length = 45)
    private String role;


}
