package com.example.manikantasales.dto;

import com.example.manikantasales.entity.Address;
import com.example.manikantasales.entity.Order;
import com.example.manikantasales.entity.OrderItem;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {

    private OrderMapper() {

    }


    public static OrderResponse toResponse(Order order) {


        OrderResponse response =
                new OrderResponse();


        // ===================================
        // ORDER BASIC DETAILS
        // ===================================

        response.setId(
                order.getId()
        );


        response.setAmount(
                order.getAmount()
        );


        response.setOrderStatus(
                order.getOrderStatus()
        );


        response.setPaymentStatus(
                order.getPaymentStatus()
        );


        response.setPaymentMethod(
                order.getPaymentMethod()
        );


        response.setCreatedDate(
                order.getCreatedDate()
        );


        // ===================================
        // DELIVERY DATE
        // ===================================

        /*
         * Remove this if Order entity
         * does not contain deliveryDate
         */

        if(order.getDeliveryDate()!=null){

            response.setDeliveryDate(
                    order.getDeliveryDate()
            );
        }



        // ===================================
        // CUSTOMER DETAILS
        // ===================================

        if(order.getUser()!=null){


            response.setUserId(
                    order.getUser().getId()
            );


            response.setCustomerEmail(
                    order.getUser().getEmail()
            );


            String firstName =
                    order.getUser()
                    .getFirstName();


            String lastName =
                    order.getUser()
                    .getLastName();



            String customerName = "";


            if(firstName!=null){

                customerName =
                        firstName.trim();
            }



            if(lastName!=null
                    && !lastName.trim().isEmpty()){


                if(!customerName.isEmpty()){

                    customerName += " ";
                }


                customerName +=
                        lastName.trim();

            }


            response.setCustomerName(
                    customerName
            );

        }



        // ===================================
        // RETURN DETAILS
        // ===================================


        response.setReturnStatus(
                order.getReturnStatus()
        );


        response.setReturnReason(
                order.getReturnReason()
        );



        // ===================================
        // ADDRESS DETAILS
        // ===================================


        if(order.getAddress()!=null){


            Address address =
                    order.getAddress();


            AddressResponse addressResponse =
                    new AddressResponse();



            addressResponse.setId(
                    address.getId()
            );


            addressResponse.setName(
                    address.getName()
            );


            addressResponse.setMobile(
                    address.getMobile()
            );


            addressResponse.setHouseNo(
                    address.getHouseNo()
            );


            addressResponse.setStreet(
                    address.getStreet()
            );


            addressResponse.setCity(
                    address.getCity()
            );


            addressResponse.setState(
                    address.getState()
            );


            addressResponse.setPincode(
                    address.getPincode()
            );


            addressResponse.setDefaultAddress(
                    address.isDefaultAddress()
            );


            addressResponse.setCreatedAt(
                    address.getCreatedAt()
            );


            addressResponse.setUpdatedAt(
                    address.getUpdatedAt()
            );


            response.setAddress(
                    addressResponse
            );

        }




        // ===================================
        // ORDER ITEMS
        // ===================================


        List<OrderItemResponse> items =
                new ArrayList<>();


        int totalQuantity = 0;



        if(order.getOrderItems()!=null){


            for(OrderItem item :
                    order.getOrderItems()){


                OrderItemResponse itemResponse =
                        new OrderItemResponse();



                // PRODUCT ID

                if(item.getProduct()!=null){


                    itemResponse.setProductId(
                            item.getProduct().getId()
                    );

                }



                // PRODUCT DETAILS


                itemResponse.setProductName(
                        item.getProductName()
                );



                String image =
                        item.getProductImage();



                if(image!=null
                        && !image.startsWith("http")){


                    if(!image.startsWith("/")){

                        image =
                                "/" + image;
                    }

                }



                itemResponse.setProductImage(
                        image
                );



                itemResponse.setPrice(
                        item.getPrice()
                );



                itemResponse.setQuantity(
                        item.getQuantity()
                );



                itemResponse.setSubTotal(
                        item.getSubTotal()
                );



                if(item.getQuantity()!=null){

                    totalQuantity +=
                            item.getQuantity();
                }



                items.add(
                        itemResponse
                );

            }

        }



        // ===================================
        // ITEMS RESPONSE
        // ===================================


        response.setItems(
                items
        );


        response.setTotalItems(
                totalQuantity
        );



        return response;

    }

}