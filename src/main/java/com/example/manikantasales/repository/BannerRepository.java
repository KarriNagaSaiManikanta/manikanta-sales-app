package com.example.manikantasales.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.manikantasales.entity.Banner;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    List<Banner> findByActiveTrue();

}