package com.example.appkhambenh.repository;

import com.example.appkhambenh.entity.TinNhan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TinNhanRepository extends JpaRepository<TinNhan, Long> {

    @Query("SELECT t FROM TinNhan t WHERE (t.nguoiGuiId = :user1 AND t.nguoiNhanId = :user2) OR (t.nguoiGuiId = :user2 AND t.nguoiNhanId = :user1) ORDER BY t.thoiGian ASC")
    List<TinNhan> findBySenderIdAndRecipientId(Integer user1, Integer user2);
    @Query("SELECT DISTINCT t.nguoiNhanId FROM TinNhan t WHERE t.nguoiGuiId = :myId")
    List<Integer> findPeopleIHaveSentTo(Integer myId);

    @Query("SELECT DISTINCT t.nguoiGuiId FROM TinNhan t WHERE t.nguoiNhanId = :myId")
    List<Integer> findPeopleWhoSentToMe(Integer myId);

    // Lấy tin nhắn mới nhất giữa 2 người (để hiện preview)
    @Query(value = "SELECT * FROM TinNhan t WHERE " +
            "(t.nguoiGuiId = :uid1 AND t.nguoiNhanId = :uid2) OR " +
            "(t.nguoiGuiId = :uid2 AND t.nguoiNhanId = :uid1) " +
            "ORDER BY t.thoiGian DESC LIMIT 1", nativeQuery = true)
    TinNhan findLatestMessage(Integer uid1, Integer uid2);
}
