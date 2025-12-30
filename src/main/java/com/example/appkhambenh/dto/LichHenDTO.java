package com.example.appkhambenh.dto;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class LichHenDTO {
    private Integer userId;
    private Integer bacSiId;
    private Integer idPhong;
    private Integer idCa;
    private LocalDate ngayKham;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private Double chiPhi;
    private String lyDoKham;
}