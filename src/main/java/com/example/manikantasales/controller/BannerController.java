package com.example.manikantasales.controller;


import com.example.manikantasales.entity.Banner;
import com.example.manikantasales.service.BannerService;

import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/banners")
@CrossOrigin("*")
public class BannerController {


private final BannerService bannerService;



public BannerController(
        BannerService bannerService
){

this.bannerService=bannerService;

}





@GetMapping
public List<Banner> getBanners(){


return bannerService.getActiveBanners();


}


}