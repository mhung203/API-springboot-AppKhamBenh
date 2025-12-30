package com.example.appkhambenh.controller;

import com.example.appkhambenh.dto.LoginRequestDTO;
import com.example.appkhambenh.dto.LoginRequestDTO;
import com.example.appkhambenh.entity.User;
import com.example.appkhambenh.entity.BenhNhan;
import com.example.appkhambenh.entity.BacSi;
import com.example.appkhambenh.repository.BacSiRepository;
import com.example.appkhambenh.repository.BenhNhanRepository;
import com.example.appkhambenh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BenhNhanRepository benhNhanRepository;

    @Autowired
    private BacSiRepository bacSiRepository;
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email này đã được sử dụng!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getIsActive() == null) user.setIsActive(true);
        if (user.getRole() == null) user.setRole("Bệnh nhân");

        User savedUser = userRepository.save(user);

        String role = savedUser.getRole();

        if ("Bệnh nhân".equalsIgnoreCase(role)) {
            createBenhNhanProfile(savedUser);
        }
        else if ("Bác sĩ".equalsIgnoreCase(role)) {
            createBacSiProfile(savedUser);
        }

        return ResponseEntity.ok(savedUser);
    }

    private void createBenhNhanProfile(User user) {
        BenhNhan bn = new BenhNhan();
        bn.setUser(user);
        String fullName = (user.getFirstname() != null ? user.getFirstname() : "") + " "
                + (user.getLastname() != null ? user.getLastname() : "");
        bn.setHoTen(fullName.trim());
        if (user.getPhone() != null) {
            bn.setSoDienThoai(user.getPhone());
        }
        if (user.getGender() != null) {
            bn.setGioiTinh(user.getGender());
        }
        bn.setMedicalHistory("Chưa có tiền sử bệnh");
        benhNhanRepository.save(bn);
    }

    private void createBacSiProfile(User user) {
        BacSi bs = new BacSi();
        bs.setUser(user);
        bs.setKinhNghiem(0);
        bs.setBangCap("Đang cập nhật");
        bacSiRepository.save(bs);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

                user.setPassword(null);
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai mật khẩu");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email không tồn tại");
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User userDetails) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setFirstname(userDetails.getFirstname());
                    user.setLastname(userDetails.getLastname());
                    user.setPhone(userDetails.getPhone());
                    user.setAddress(userDetails.getAddress());
                    user.setBirthday(userDetails.getBirthday());
                    user.setGender(userDetails.getGender());
                    User updatedUser = userRepository.save(user);
                    String fullName = (user.getFirstname() != null ? user.getFirstname() : "") + " "
                            + (user.getLastname() != null ? user.getLastname() : "");
                    String finalfullName = fullName.trim();
                    if ("Bệnh nhân".equalsIgnoreCase(user.getRole())) {
                        benhNhanRepository.findById(id).ifPresent(bn -> {
                            bn.setHoTen(finalfullName);
                            bn.setSoDienThoai(user.getPhone());
                            bn.setGioiTinh(user.getGender());
                            benhNhanRepository.save(bn);
                        });
                    }
                    return ResponseEntity.ok(updatedUser);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}