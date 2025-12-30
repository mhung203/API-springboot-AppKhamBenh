package com.example.appkhambenh.service;

import com.example.appkhambenh.entity.TinTuc;
import com.example.appkhambenh.repository.TinTucRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NewsCrawlerService {

    @Autowired
    private TinTucRepository tinTucRepository;

    private static final String RSS_URL = "https://vnexpress.net/rss/suc-khoe.rss";

    @Scheduled(initialDelay = 2000, fixedRate = 1800000)
    public void crawlNewsData() {
        System.out.println(">>> [BOT] Bắt đầu quét tin tức y tế mới...");

        try {

            Document doc = Jsoup.connect(RSS_URL).get();


            Elements items = doc.select("item");

            int countNew = 0;

            for (Element item : items) {
                String link = item.select("link").text();


                if (!tinTucRepository.existsByLinkBaiViet(link)) {

                    String title = item.select("title").text();
                    String descriptionRaw = item.select("description").text();


                    TinTuc tinTuc = new TinTuc();
                    tinTuc.setTieuDe(title);
                    tinTuc.setLinkBaiViet(link);
                    tinTuc.setNgayDang(LocalDateTime.now());


                    Document descDoc = Jsoup.parse(descriptionRaw);


                    Element imgTag = descDoc.select("img").first();
                    if (imgTag != null) {
                        tinTuc.setHinhAnh(imgTag.attr("src"));
                    } else {
                        tinTuc.setHinhAnh("https://i.imgur.com/P60QkKq.png"); // Ảnh mặc định nếu lỗi
                    }


                    String cleanText = descDoc.body().text();
                    tinTuc.setMoTaNgan(cleanText);


                    tinTucRepository.save(tinTuc);
                    countNew++;
                }
            }

            if (countNew > 0) {
                System.out.println(">>> [BOT] Đã thêm thành công " + countNew + " tin mới!");
            } else {
                System.out.println(">>> [BOT] Không có tin mới nào.");
            }

        } catch (Exception e) {
            System.err.println(">>> [BOT] Lỗi khi cào tin: " + e.getMessage());
        }
    }
}