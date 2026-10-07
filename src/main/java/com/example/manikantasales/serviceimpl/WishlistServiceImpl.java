package com.example.manikantasales.serviceimpl;


import com.example.manikantasales.entity.*;
import com.example.manikantasales.repository.*;
import com.example.manikantasales.service.WishlistService;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;



@Service
public class WishlistServiceImpl 
implements WishlistService {



    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;



    public WishlistServiceImpl(
            WishlistRepository wishlistRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ){

        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;

    }





    private User getUser(){


        String email =
                SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();



        return userRepository
                .findByEmail(email)
                .orElseThrow(
                    () -> new RuntimeException("User not found")
                );

    }







    @Override
    public Wishlist addWishlist(Long productId){


        User user = getUser();



        Product product =
                productRepository
                .findById(productId)
                .orElseThrow();



        if(wishlistRepository
                .existsByUserAndProduct(user, product)){


            throw new RuntimeException(
                    "Already in wishlist"
            );

        }




        Wishlist wishlist = new Wishlist();


        wishlist.setUser(user);

        wishlist.setProduct(product);



        return wishlistRepository.save(wishlist);

    }








    @Override
    public List<Wishlist> getMyWishlist(){


        return wishlistRepository
                .findByUser(getUser());

    }








    // ===============================
    // REMOVE WISHLIST
    // ===============================

    @Override
    @Transactional
    public void removeWishlist(Long productId){



        User user = getUser();



        Product product =
                productRepository
                .findById(productId)
                .orElseThrow(
                    () -> new RuntimeException(
                        "Product not found"
                    )
                );



        if(!wishlistRepository
                .existsByUserAndProduct(user, product)){


            throw new RuntimeException(
                    "Wishlist item not found"
            );

        }



        wishlistRepository
                .deleteByUserAndProduct(
                        user,
                        product
                );


    }


}