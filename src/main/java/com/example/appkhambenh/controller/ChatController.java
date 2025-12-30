package com.example.appkhambenh.controller;

import com.example.appkhambenh.dto.ChatContactDTO;
import com.example.appkhambenh.dto.ChatMessageDTO;
import com.example.appkhambenh.entity.BacSi;
import com.example.appkhambenh.entity.TinNhan;
import com.example.appkhambenh.entity.User;
import com.example.appkhambenh.repository.BacSiRepository;
import com.example.appkhambenh.repository.TinNhanRepository;
import com.example.appkhambenh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private TinNhanRepository tinNhanRepository;
    @Autowired
    private BacSiRepository bacSiRepository;

    // 1. Xử lý nhận tin nhắn từ Android gửi lên qua WebSocket
    // Client gửi tới: /app/chat
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDTO chatMessage) {

        // A. Lưu tin nhắn vào Database
        TinNhan tinNhanMoi = new TinNhan(
                chatMessage.getSenderId(),
                chatMessage.getRecipientId(),
                chatMessage.getContent()
        );
        TinNhan saved = tinNhanRepository.save(tinNhanMoi);

        // B. Gửi tin nhắn đó ĐẾN NGƯỜI NHẬN ngay lập tức (Real-time)
        // Đường dẫn người nhận sẽ lắng nghe: /user/{userId}/queue/messages
        messagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getRecipientId()), // ID người nhận
                "/queue/messages", // Hàng đợi
                saved // Dữ liệu gửi về
        );
    }

    // 2. API lấy lại lịch sử chat cũ (Gọi bằng Retrofit như bình thường)
    @GetMapping("/api/messages/{senderId}/{recipientId}")
    @ResponseBody
    public List<TinNhan> getChatHistory(@PathVariable Integer senderId, @PathVariable Integer recipientId) {
        return tinNhanRepository.findBySenderIdAndRecipientId(senderId, recipientId);
    }
    @Autowired
    private UserRepository userRepository; // Cần cái này để lấy tên và avatar

    // API: Lấy danh sách những người đã chat với mình (Inbox)
    // GET /api/chat/contacts/3 (Nếu mình là User ID 3)
    @GetMapping("/api/chat/contacts/{myId}")
    @ResponseBody
    public List<ChatContactDTO> getContactList(@PathVariable Integer myId) {

        List<Integer> sentTo = tinNhanRepository.findPeopleIHaveSentTo(myId);
        List<Integer> receivedFrom = tinNhanRepository.findPeopleWhoSentToMe(myId);


        Set<Integer> contactIds = new HashSet<>();
        contactIds.addAll(sentTo);
        contactIds.addAll(receivedFrom);

        List<ChatContactDTO> result = new ArrayList<>();


        for (Integer otherId : contactIds) {

            User otherUser = userRepository.findById(otherId).orElse(null);


            TinNhan lastMsg = tinNhanRepository.findLatestMessage(myId, otherId);

            if (otherUser != null && lastMsg != null) {
                String name = otherUser.getLastname() + " " + otherUser.getFirstname();
                String avatar = "default_avatar.png";

                result.add(new ChatContactDTO(
                        otherId,
                        name,
                        avatar,
                        lastMsg.getNoiDung(),
                        lastMsg.getThoiGian()
                ));
            }
        }
        result.sort((a, b) -> b.getTime().compareTo(a.getTime()));

        return result;
    }
    @GetMapping("/api/chat/all-doctors")
    @ResponseBody
    public List<ChatContactDTO> getAllDoctorsForChat() {
        List<BacSi> listBacSi = bacSiRepository.findAll();
        List<ChatContactDTO> result = new ArrayList<>();

        for (BacSi bs : listBacSi) {
            if (bs.getUser() != null) {
                Integer chatUserId = bs.getUser().getUserId();
                String ho = (bs.getUser().getLastname() != null) ? bs.getUser().getLastname() : "";
                String ten = (bs.getUser().getFirstname() != null) ? bs.getUser().getFirstname() : "";
                String fullName = (ho + " " + ten).trim();
                String avatar = bs.getAnhDaiDien();
                if (avatar == null || avatar.isEmpty()) {
                    avatar = "default.avatar.png";
                }
                String chuyenKhoa = (bs.getKhoa() != null) ? bs.getKhoa().getTenKhoa() : "Bác sĩ";
                result.add(new ChatContactDTO(
                        chatUserId,
                        fullName,
                        avatar,
                        "Chuyên khoa: " + chuyenKhoa,
                        null
                ));
            }
        }
        return result;
    }
    @GetMapping("/api/chat/all-patients")
    @ResponseBody
    public List<ChatContactDTO> getAllPatientsForChat() {

        List<User> listUsers = userRepository.findAll();
        List<ChatContactDTO> result = new ArrayList<>();

        for (User u : listUsers) {

            String role = u.getRole();
            if (role != null && !role.equalsIgnoreCase("doctor") && !role.equalsIgnoreCase("admin")) {

                String ho = (u.getLastname() != null) ? u.getLastname() : "";
                String ten = (u.getFirstname() != null) ? u.getFirstname() : "";
                String fullName = (ho + " " + ten).trim();

                if (fullName.isEmpty()) fullName = "Bệnh nhân (Chưa có tên)";

                result.add(new ChatContactDTO(
                        u.getUserId(),
                        fullName,
                        "default_avatar.png",
                        "Bệnh nhân", // Dòng mô tả
                        null
                ));
            }
        }
        return result;
    }
}
