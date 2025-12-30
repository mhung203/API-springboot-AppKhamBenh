package com.example.appkhambenh.controller;

import com.example.appkhambenh.entity.TinTuc;
import com.example.appkhambenh.repository.TinTucRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/news")
public class TinTucController {

    @Autowired
    private TinTucRepository tinTucRepository;
    @GetMapping("/latest")
    public List<TinTuc> getLatestNews() {
        return tinTucRepository.findAll(
                PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "ngayDang"))
        ).getContent();
    }
}