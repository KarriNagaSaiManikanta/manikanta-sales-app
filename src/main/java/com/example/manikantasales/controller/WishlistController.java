package com.example.manikantasales.controller;


import com.example.manikantasales.entity.Wishlist;
import com.example.manikantasales.service.WishlistService;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;



@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin("*")
public class WishlistController {



private final WishlistService wishlistService;



public WishlistController(
        WishlistService wishlistService
){

this.wishlistService=wishlistService;

}





@PostMapping("/add")
public ResponseEntity<?> addWishlist(
        @RequestParam Long productId
){


return ResponseEntity.ok(

wishlistService.addWishlist(productId)

);


}





@GetMapping("/my")
public ResponseEntity<List<Wishlist>> getWishlist(){


return ResponseEntity.ok(

wishlistService.getMyWishlist()

);


}




@DeleteMapping("/remove")
public ResponseEntity<?> remove(
        @RequestParam Long productId
){


wishlistService.removeWishlist(productId);


return ResponseEntity.ok(
"Removed"
);


}



}