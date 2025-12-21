package com.example.appkhambenh.controller;

import com.example.appkhambenh.dto.LoginRequestDTO; // Đảm bảo bạn đã tạo file DTO này
import com.example.appkhambenh.dto.LoginRequestDTO;
import com.example.appkhambenh.entity.User;
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
    private PasswordEncoder passwordEncoder; // Tiêm bộ mã hóa (đã cấu hình bên SecurityConfig)


    // 1. ĐĂNG KÝ (Thay thế cho hàm createUser cũ)
    // POST: http://localhost:8081/api/users/register
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Kiểm tra xem email đã tồn tại chưa
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email này đã được sử dụng!");
        }

        // --- QUAN TRỌNG: MÃ HÓA MẬT KHẨU ---
        // Biến "123" thành "$2a$10$..."
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Thiết lập mặc định nếu thiếu
        if (user.getIsActive() == null) user.setIsActive(true); // Mặc định cho active luôn để test
        if (user.getRole() == null) user.setRole("Bệnh nhân");

        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(savedUser);
    }

    // 2. ĐĂNG NHẬP (Mới hoàn toàn)
    // POST: http://localhost:8081/api/users/login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        // Tìm user theo email
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // So sánh mật khẩu (Chưa mã hóa vs Đã mã hóa)
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                // Đăng nhập thành công
                user.setPassword(null); // Xóa pass trước khi trả về để bảo mật
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sai mật khẩu");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email không tồn tại");
        }
    }

    // ================== CÁC CHỨC NĂNG QUẢN LÝ (CRUD CŨ - GIỮ LẠI) ==================

    // 3. Lấy danh sách (Giữ nguyên)
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 4. Lấy chi tiết (Giữ nguyên)
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. Cập nhật User (Giữ nguyên logic cũ của bạn)
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

                    // Lưu ý: Không cho update password ở đây (cần API đổi pass riêng)

                    User updatedUser = userRepository.save(user);
                    return ResponseEntity.ok().body(updatedUser);
                }).orElse(ResponseEntity.notFound().build());
    }

    // 6. Xóa User (Giữ nguyên)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}