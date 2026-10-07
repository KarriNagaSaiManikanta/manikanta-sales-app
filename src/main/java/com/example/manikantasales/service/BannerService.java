package com.example.manikantasales.service;

import java.util.List;
import com.example.manikantasales.entity.Banner;

public interface BannerService {

    Banner saveBanner(Banner banner);

    Banner updateBanner(Long id, Banner banner);

    void deleteBanner(Long id);

    Banner getBannerById(Long id);

    List<Banner> getAllBanners();

    // ADD THIS
    List<Banner> getActiveBanners();

}