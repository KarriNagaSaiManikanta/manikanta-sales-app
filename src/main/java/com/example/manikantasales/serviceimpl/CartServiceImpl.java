package com.example.manikantasales.serviceimpl;


import com.example.manikantasales.dto.CartResponse;
import com.example.manikantasales.entity.Cart;
import com.example.manikantasales.entity.Product;
import com.example.manikantasales.entity.User;
import com.example.manikantasales.repository.CartRepository;
import com.example.manikantasales.repository.ProductRepository;
import com.example.manikantasales.repository.UserRepository;
import com.example.manikantasales.service.CartService;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



@Service
public class CartServiceImpl implements CartService {


    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;



    public CartServiceImpl(
            CartRepository cartRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ){

        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;

    }





    // =====================================
    // GET LOGGED USER
    // =====================================

    private User getLoggedInUser(){


        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();



        if(authentication == null ||
                !authentication.isAuthenticated()){

            throw new RuntimeException(
                    "User not authenticated"
            );

        }



        String email =
                authentication.getName();



        return userRepository.findByEmail(email)

                .orElseThrow(() ->

                        new RuntimeException(
                                "User not found : "+email
                        )

                );

    }








    // =====================================
    // ADD CART
    // =====================================

    @Override
    public Cart addToCart(
            Long productId,
            Integer quantity
    ){


        User user = getLoggedInUser();



        Product product =

                productRepository.findById(productId)

                .orElseThrow(() ->

                        new RuntimeException(
                                "Product not found"
                        )

                );



        Integer stock =
                product.getQuantity();



        if(stock == null || stock <= 0){

            throw new RuntimeException(
                    "Product out of stock"
            );

        }



        if(quantity <=0){

            throw new RuntimeException(
                    "Invalid quantity"
            );

        }



        if(quantity > stock){

            throw new RuntimeException(
                    "Only "
                    +stock+
                    " items available"
            );

        }




        Cart cart =

                cartRepository.findByUserAndProduct(
                        user,
                        product
                )
                .orElse(null);





        if(cart != null){


            int newQuantity =
                    cart.getQuantity()+quantity;



            if(newQuantity > stock){

                throw new RuntimeException(
                        "Only "
                        +stock+
                        " items available"
                );

            }



            cart.setQuantity(
                    newQuantity
            );


        }

        else{


            cart = new Cart(
                    user,
                    product,
                    quantity
            );

        }



        return cartRepository.save(cart);

    }









    // =====================================
    // GET CART
    // =====================================

    @Override
    public List<CartResponse> getMyCart(){



        User user = getLoggedInUser();



        List<Cart> cartList =
                cartRepository.findCartWithProduct(user);



        List<CartResponse> response =
                new ArrayList<>();




        for(Cart cart : cartList){


            Product product =
                    cart.getProduct();



            if(product == null){
                continue;
            }




            CartResponse dto =
                    new CartResponse();



            dto.setCartId(
                    cart.getId()
            );

            dto.setProductId(
                    product.getId()
            );

            dto.setProductName(
                    product.getName()
            );

            dto.setProductImage(
                    product.getImage()
            );

            dto.setBrand(
                    product.getBrand()
            );



            if(product.getCategory()!=null){

                dto.setCategory(
                        product.getCategory().getName()
                );

            }
            else{

                dto.setCategory(
                        "No Category"
                );

            }




            BigDecimal price =
                    product.getPrice();



            if(price == null){

                price = BigDecimal.ZERO;

            }



            dto.setPrice(price);



            dto.setQuantity(
                    cart.getQuantity()
            );



            dto.setStock(
                    product.getQuantity()
            );



            dto.setTotalPrice(

                    price.multiply(

                            BigDecimal.valueOf(
                                    cart.getQuantity()
                            )

                    )

            );



            response.add(dto);


        }



        return response;

    }









    // =====================================
    // INCREASE
    // =====================================

    @Override
    public Cart increaseQuantity(Long cartId){


        User user =
                getLoggedInUser();



        Cart cart =

                cartRepository.findById(cartId)

                .orElseThrow(() ->

                        new RuntimeException(
                                "Cart item not found"
                        )

                );



        if(!cart.getUser()
                .getId()
                .equals(user.getId())){


            throw new RuntimeException(
                    "Unauthorized cart access"
            );

        }




        Product product =
                cart.getProduct();



        Integer stock =
                product.getQuantity();



        if(cart.getQuantity() >= stock){


            throw new RuntimeException(
                    "Only "
                    +stock+
                    " items available"
            );

        }



        cart.setQuantity(
                cart.getQuantity()+1
        );



        return cartRepository.save(cart);

    }









    // =====================================
    // DECREASE
    // =====================================

    @Override
    public Cart decreaseQuantity(Long cartId){


        User user =
                getLoggedInUser();



        Cart cart =

                cartRepository.findById(cartId)

                .orElseThrow(() ->

                        new RuntimeException(
                                "Cart item not found"
                        )

                );



        if(!cart.getUser()
                .getId()
                .equals(user.getId())){


            throw new RuntimeException(
                    "Unauthorized cart access"
            );

        }




        if(cart.getQuantity()>1){


            cart.setQuantity(
                    cart.getQuantity()-1
            );


            return cartRepository.save(cart);

        }



        cartRepository.delete(cart);



        return null;

    }









    // =====================================
    // REMOVE
    // =====================================

    @Override
    public void removeCartItem(Long cartId){


        User user =
                getLoggedInUser();



        Cart cart =

                cartRepository.findById(cartId)

                .orElseThrow(() ->

                        new RuntimeException(
                                "Cart item not found"
                        )

                );



        if(!cart.getUser()
                .getId()
                .equals(user.getId())){


            throw new RuntimeException(
                    "Unauthorized cart access"
            );

        }



        cartRepository.delete(cart);

    }









    // =====================================
    // CLEAR
    // =====================================

    @Override
    public void clearCart(){


        cartRepository.deleteByUser(
                getLoggedInUser()
        );

    }









    // =====================================
    // COUNT
    // =====================================

    @Override
    public long getCartCount(){


        return cartRepository.getTotalQuantityByUser(
                getLoggedInUser()
        );

    }


}