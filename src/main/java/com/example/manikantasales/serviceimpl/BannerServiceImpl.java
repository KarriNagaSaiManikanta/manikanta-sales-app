package com.example.manikantasales.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.manikantasales.entity.Banner;
import com.example.manikantasales.repository.BannerRepository;
import com.example.manikantasales.service.BannerService;

@Service
public class BannerServiceImpl implements BannerService {

    private final BannerRepository bannerRepository;

    public BannerServiceImpl(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    @Override
    public Banner saveBanner(Banner banner) {
        return bannerRepository.save(banner);
    }

    @Override
    public Banner updateBanner(Long id, Banner banner) {
        Banner old = bannerRepository.findById(id).orElseThrow();

        old.setTitle(banner.getTitle());
        old.setDescription(banner.getDescription());
        old.setImage(banner.getImage());
        old.setCategory(banner.getCategory());
        old.setOffer(banner.getOffer());
        old.setActive(banner.isActive());

        return bannerRepository.save(old);
    }

    @Override
    public void deleteBanner(Long id) {
        bannerRepository.deleteById(id);
    }

    @Override
    public Banner getBannerById(Long id) {
        return bannerRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }

    @Override
    public List<Banner> getActiveBanners() {
        return bannerRepository.findByActiveTrue();
    }

}