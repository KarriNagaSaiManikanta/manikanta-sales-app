package com.example.manikantasales.serviceimpl;

import com.example.manikantasales.dto.OrderMapper;
import com.example.manikantasales.dto.OrderRequest;
import com.example.manikantasales.dto.OrderResponse;
import com.example.manikantasales.entity.Address;
import com.example.manikantasales.entity.Cart;
import com.example.manikantasales.entity.Order;
import com.example.manikantasales.entity.OrderItem;
import com.example.manikantasales.entity.Product;
import com.example.manikantasales.entity.User;

import com.example.manikantasales.enums.OrderStatus;
import com.example.manikantasales.enums.PaymentMethod;
import com.example.manikantasales.enums.PaymentStatus;
import com.example.manikantasales.enums.ReturnStatus;

import com.example.manikantasales.repository.AddressRepository;
import com.example.manikantasales.repository.CartRepository;
import com.example.manikantasales.repository.OrderRepository;
import com.example.manikantasales.repository.ProductRepository;
import com.example.manikantasales.repository.UserRepository;

import com.example.manikantasales.service.OrderAsyncService;
import com.example.manikantasales.service.OrderService;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional
public class OrderServiceImpl implements OrderService {


    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final OrderAsyncService orderAsyncService;



    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public OrderServiceImpl(
            OrderRepository orderRepository,
            CartRepository cartRepository,
            ProductRepository productRepository,
            AddressRepository addressRepository,
            UserRepository userRepository,
            OrderAsyncService orderAsyncService
    ) {

        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.orderAsyncService = orderAsyncService;
    }



    // =====================================================
    // GET LOGGED IN USER
    // =====================================================

    private User getLoggedInUser() {


        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();


        if(authentication == null
                || !authentication.isAuthenticated()) {

            throw new RuntimeException(
                    "User not logged in"
            );
        }


        String email =
                authentication.getName();


        return userRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "User not found : "
                                + email
                        )
                );
    }





    // =====================================================
    // PLACE ORDER
    // =====================================================

    @Override
    @Transactional
    public OrderResponse placeOrder(
            OrderRequest request
    ) {


        User user =
                getLoggedInUser();



        Address address =
                addressRepository
                        .findById(
                                request.getAddressId()
                        )
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Address not found"
                                )
                        );



        List<Cart> cartItems =
                cartRepository
                        .findCartWithProduct(user);



        if(cartItems.isEmpty()) {

            throw new RuntimeException(
                    "Cart is empty"
            );
        }



        Order order =
                new Order();


        order.setUser(user);

        order.setAddress(address);

        order.setOrderStatus(
                OrderStatus.PENDING
        );



        // PAYMENT

        if(request.getPaymentMethod()
                == PaymentMethod.COD) {


            order.setPaymentStatus(
                    PaymentStatus.PENDING
            );


        } else {


            order.setPaymentStatus(
                    PaymentStatus.PAID
            );

        }



        order.setPaymentMethod(
                request.getPaymentMethod()
        );




        List<OrderItem> orderItems =
                new ArrayList<>();


        List<Product> updateProducts =
                new ArrayList<>();


        BigDecimal total =
                BigDecimal.ZERO;




        for(Cart cart : cartItems) {


            Product product =
                    cart.getProduct();



            int quantity =
                    cart.getQuantity();



            if(product.getQuantity()
                    < quantity) {


                throw new RuntimeException(
                        product.getName()
                        +" stock unavailable"
                );
            }




            BigDecimal subTotal =
                    product.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            quantity
                                    )
                            );



            total =
                    total.add(subTotal);




            OrderItem item =
                    new OrderItem();



            item.setOrder(order);

            item.setProduct(product);

            item.setProductName(
                    product.getName()
            );


            item.setProductImage(
                    product.getImage()
            );


            item.setPrice(
                    product.getPrice()
            );


            item.setQuantity(
                    quantity
            );


            item.setSubTotal(
                    subTotal
            );



            orderItems.add(item);




            // STOCK UPDATE

            product.setQuantity(
                    product.getQuantity()
                    - quantity
            );


            updateProducts.add(product);

        }




        // SHIPPING

        BigDecimal shipping =
                BigDecimal.valueOf(
                        request.getShippingCharge()
                );


        total =
                total.add(shipping);



        order.setAmount(total);


        order.setOrderItems(
                orderItems
        );




        Order savedOrder =
                orderRepository.save(order);



        productRepository.saveAll(
                updateProducts
        );



        cartRepository.deleteByUser(user);




        // EMAIL

        orderAsyncService
                .sendOrderConfirmationEmail(
                        savedOrder
                );



        return OrderMapper
                .toResponse(savedOrder);

    }






    // =====================================================
    // MY ORDERS
    // =====================================================


    @Override
    public List<OrderResponse> getMyOrders() {


        User user =
                getLoggedInUser();



        List<Order> orders =
                orderRepository
                        .findByUserOrderByCreatedDateDesc(
                                user
                        );



        return orders.stream()
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());

    }






    // =====================================================
    // MY ORDER DETAILS
    // =====================================================


    @Override
    public OrderResponse getMyOrderById(
            Long orderId
    ) {


        User user =
                getLoggedInUser();



        Order order =
                orderRepository
                        .findByIdAndUser(
                                orderId,
                                user
                        )
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Order not found"
                                )
                        );



        return OrderMapper
                .toResponse(order);

    }
 // =====================================================
 // CANCEL ORDER
 // USER
 // =====================================================

 @Override
 public OrderResponse cancelOrder(
         Long orderId
 ) {


     User user =
             getLoggedInUser();



     Order order =
             orderRepository
                     .findByIdAndUser(
                             orderId,
                             user
                     )
                     .orElseThrow(() ->
                             new EntityNotFoundException(
                                     "Order not found"
                             )
                     );



     if(order.getOrderStatus()
             == OrderStatus.CANCELLED) {


         throw new RuntimeException(
                 "Order already cancelled"
         );
     }



     if(order.getOrderStatus()
             != OrderStatus.PENDING
             &&
             order.getOrderStatus()
             != OrderStatus.CONFIRMED) {


         throw new RuntimeException(
                 "Only pending or confirmed orders can be cancelled"
         );

     }




     // RESTORE STOCK

     for(OrderItem item :
             order.getOrderItems()) {


         Product product =
                 item.getProduct();


         product.setQuantity(
                 product.getQuantity()
                 + item.getQuantity()
         );


         productRepository.save(product);

     }




     order.setOrderStatus(
             OrderStatus.CANCELLED
     );



     Order updatedOrder =
             orderRepository.save(order);




     orderAsyncService
             .sendOrderCancellationEmail(
                     user.getEmail(),
                     user.getFirstName(),
                     updatedOrder.getId()
             );



     return OrderMapper
             .toResponse(updatedOrder);

 }






 // =====================================================
 // GET ALL ORDERS
 // ADMIN
 // =====================================================

 @Override
 public List<OrderResponse> getAllOrders() {


     return orderRepository
             .findAll()
             .stream()
             .map(OrderMapper::toResponse)
             .collect(Collectors.toList());

 }







 // =====================================================
 // GET ORDER BY ID
 // ADMIN
 // =====================================================

 @Override
 public OrderResponse getOrderById(
         Long orderId
 ) {


     Order order =
             orderRepository
                     .findById(orderId)
                     .orElseThrow(() ->
                             new EntityNotFoundException(
                                     "Order not found : "
                                     + orderId
                             )
                     );



     return OrderMapper
             .toResponse(order);

 }







 // =====================================================
 // UPDATE ORDER STATUS
 // ADMIN
 // =====================================================

 @Override
 @Transactional
 public OrderResponse updateOrderStatus(
         Long orderId,
         OrderStatus orderStatus
 ) {


     Order order =
             orderRepository
                     .findById(orderId)
                     .orElseThrow(() ->
                             new EntityNotFoundException(
                                     "Order not found"
                             )
                     );



     order.setOrderStatus(
             orderStatus
     );



     /*
      * COD FLOW
      *
      * PENDING          -> PAYMENT PENDING
      * CONFIRMED        -> PAYMENT PENDING
      * PROCESSING       -> PAYMENT PENDING
      * SHIPPED          -> PAYMENT PENDING
      * OUT_FOR_DELIVERY -> PAYMENT PENDING
      * DELIVERED        -> PAYMENT PAID
      */


     if(orderStatus == OrderStatus.DELIVERED) {


         order.setPaymentStatus(
                 PaymentStatus.PAID
         );


     } else {


         order.setPaymentStatus(
                 PaymentStatus.PENDING
         );

     }




     Order updatedOrder =
             orderRepository.save(order);




     String email =
             updatedOrder
                     .getUser()
                     .getEmail();


     String firstName =
             updatedOrder
                     .getUser()
                     .getFirstName();


     String status =
             updatedOrder
                     .getOrderStatus()
                     .name();




     OrderResponse response =
             OrderMapper
                     .toResponse(updatedOrder);




     orderAsyncService
             .sendOrderStatusEmail(
                     email,
                     firstName,
                     updatedOrder.getId(),
                     status
             );



     return response;

 }








 // =====================================================
 // UPDATE PAYMENT STATUS
 // ADMIN
 // =====================================================

 @Override
 public OrderResponse updatePaymentStatus(
         Long orderId,
         PaymentStatus paymentStatus
 ) {


     Order order =
             orderRepository
                     .findById(orderId)
                     .orElseThrow(() ->
                             new EntityNotFoundException(
                                     "Order not found"
                             )
                     );



     order.setPaymentStatus(
             paymentStatus
     );



     Order updatedOrder =
             orderRepository.save(order);



     return OrderMapper
             .toResponse(updatedOrder);

 }







 // =====================================================
 // DELETE ORDER
 // ADMIN
 // =====================================================

 @Override
 public void deleteOrder(
         Long orderId
 ) {


     Order order =
             orderRepository
                     .findById(orderId)
                     .orElseThrow(() ->
                             new EntityNotFoundException(
                                     "Order not found"
                             )
                     );


     orderRepository.delete(order);

 }







 // =====================================================
 // FIND ORDER ENTITY
 // INTERNAL
 // =====================================================

 @Override
 public Order findOrderEntityById(
         Long orderId
 ) {


     return orderRepository
             .findById(orderId)
             .orElseThrow(() ->
                     new EntityNotFoundException(
                             "Order not found : "
                             + orderId
                     )
             );

 }







 // =====================================================
 // TOTAL ORDERS COUNT
 // =====================================================

 @Override
 public Long getOrderCount() {


     return orderRepository.count();

 }







 // =====================================================
 // TOTAL REVENUE
 // =====================================================

 @Override
 public BigDecimal getTotalRevenue() {


     BigDecimal revenue =
             orderRepository.getTotalRevenue();



     return revenue != null
             ? revenue
             : BigDecimal.ZERO;

 }







 // =====================================================
 // TODAY REVENUE
 // =====================================================

 @Override
 public BigDecimal getTodayRevenue() {


     BigDecimal revenue =
             orderRepository.getTodayRevenue();



     return revenue != null
             ? revenue
             : BigDecimal.ZERO;

 }







 // =====================================================
 // MONTH REVENUE
 // =====================================================

 @Override
 public BigDecimal getMonthRevenue() {


     BigDecimal revenue =
             orderRepository.getMonthRevenue();



     return revenue != null
             ? revenue
             : BigDecimal.ZERO;

 }
//=====================================================
//RECENT ORDERS
//ADMIN DASHBOARD
//=====================================================

@Override
@Transactional(readOnly = true)
public List<OrderResponse> getRecentOrders() {


  return orderRepository
          .findRecentOrders()
          .stream()
          .limit(10)
          .map(OrderMapper::toResponse)
          .collect(Collectors.toList());

}






//=====================================================
//REVENUE CHART
//ADMIN DASHBOARD
//=====================================================

@Override
@Transactional(readOnly = true)
public Map<String,Object> getRevenueChart(
      String type
) {


  Map<String,Object> response =
          new HashMap<>();


  List<String> labels =
          new ArrayList<>();


  List<BigDecimal> values =
          new ArrayList<>();




  if("daily".equalsIgnoreCase(type)) {


      List<Object[]> data =
              orderRepository.dailyRevenue();



      for(Object[] row : data) {


          labels.add(
                  String.valueOf(row[0])
          );


          values.add(
                  new BigDecimal(
                          String.valueOf(row[1])
                  )
          );

      }



  }
  else if("weekly".equalsIgnoreCase(type)) {


      List<Object[]> data =
              orderRepository.weeklyRevenue();



      for(Object[] row : data) {


          labels.add(
                  "Week "
                  + row[0]
          );


          values.add(
                  new BigDecimal(
                          String.valueOf(row[1])
                  )
          );

      }



  }
  else if("monthly".equalsIgnoreCase(type)) {


      List<Object[]> data =
              orderRepository.monthlyRevenue();



      for(Object[] row : data) {


          labels.add(
                  "Month "
                  + row[0]
          );


          values.add(
                  new BigDecimal(
                          String.valueOf(row[1])
                  )
          );

      }



  }
  else {


      throw new IllegalArgumentException(
              "Invalid chart type"
      );

  }



  response.put(
          "labels",
          labels
  );


  response.put(
          "values",
          values
  );


  return response;

}







//=====================================================
//REQUEST RETURN
//USER
//=====================================================

@Override
public OrderResponse requestReturn(
      Long orderId,
      String returnReason
) {


  Order order =
          orderRepository
                  .findById(orderId)
                  .orElseThrow(() ->
                          new EntityNotFoundException(
                                  "Order not found"
                          )
                  );



  if(order.getOrderStatus()
          != OrderStatus.DELIVERED) {


      throw new RuntimeException(
              "Only delivered orders can be returned"
      );

  }



  if(returnReason == null
          || returnReason.trim().isEmpty()) {


      throw new RuntimeException(
              "Return reason is required"
      );

  }



  if(order.getReturnStatus()!=null
          &&
          order.getReturnStatus()
                  != ReturnStatus.NONE) {


      throw new RuntimeException(
              "Return request already exists"
      );

  }




  order.setReturnStatus(
          ReturnStatus.REQUESTED
  );


  order.setReturnReason(
          returnReason.trim()
  );


  order.setPaymentStatus(
          PaymentStatus.PAID
  );




  Order updatedOrder =
          orderRepository.save(order);




  orderAsyncService
          .sendReturnRequestEmail(
                  updatedOrder.getUser().getEmail(),
                  updatedOrder.getUser().getFirstName(),
                  updatedOrder.getId(),
                  updatedOrder.getReturnReason()
          );



  return OrderMapper
          .toResponse(updatedOrder);

}







//=====================================================
//APPROVE RETURN
//ADMIN
//=====================================================

@Override
public OrderResponse approveReturn(
      Long orderId
) {


  Order order =
          orderRepository
                  .findById(orderId)
                  .orElseThrow(() ->
                          new EntityNotFoundException(
                                  "Order not found"
                          )
                  );



  if(order.getReturnStatus()
          != ReturnStatus.REQUESTED) {


      throw new RuntimeException(
              "Return request is not pending"
      );

  }




  order.setReturnStatus(
          ReturnStatus.APPROVED
  );


  order.setPaymentStatus(
          PaymentStatus.PAID
  );




  Order updated =
          orderRepository.save(order);




  orderAsyncService
          .sendReturnApprovedEmail(
                  updated.getUser().getEmail(),
                  updated.getUser().getFirstName(),
                  updated.getId(),
                  updated.getAmount().toString(),
                  updated.getReturnStatus().name()
          );



  return OrderMapper
          .toResponse(updated);

}








//=====================================================
//REJECT RETURN
//ADMIN
//=====================================================

@Override
public OrderResponse rejectReturn(
      Long orderId
) {


  Order order =
          orderRepository
                  .findById(orderId)
                  .orElseThrow(() ->
                          new EntityNotFoundException(
                                  "Order not found"
                          )
                  );



  if(order.getReturnStatus()
          != ReturnStatus.REQUESTED) {


      throw new RuntimeException(
              "Return request is not pending"
      );

  }



  order.setReturnStatus(
          ReturnStatus.REJECTED
  );


  order.setPaymentStatus(
          PaymentStatus.PAID
  );




  Order updated =
          orderRepository.save(order);




  orderAsyncService
          .sendReturnRejectedEmail(
                  updated.getUser().getEmail(),
                  updated.getUser().getFirstName(),
                  updated.getId(),
                  updated.getAmount().toString(),
                  updated.getReturnStatus().name()
          );



  return OrderMapper
          .toResponse(updated);

}








//=====================================================
//COMPLETE RETURN
//ADMIN
//=====================================================

@Override
public OrderResponse completeReturn(
      Long orderId
) {


  Order order =
          orderRepository
                  .findById(orderId)
                  .orElseThrow(() ->
                          new EntityNotFoundException(
                                  "Order not found"
                          )
                  );



  if(order.getReturnStatus()
          != ReturnStatus.APPROVED) {


      throw new RuntimeException(
              "Return must be approved before completion"
      );

  }




  order.setReturnStatus(
          ReturnStatus.RETURNED
  );



  order.setPaymentStatus(
          PaymentStatus.REFUNDED
  );




  Order updated =
          orderRepository.save(order);




  orderAsyncService
          .sendReturnCompletedEmail(
                  updated.getUser().getEmail(),
                  updated.getUser().getFirstName(),
                  updated.getId(),
                  updated.getAmount().toString(),
                  updated.getReturnStatus().name()
          );



  return OrderMapper
          .toResponse(updated);

}

}