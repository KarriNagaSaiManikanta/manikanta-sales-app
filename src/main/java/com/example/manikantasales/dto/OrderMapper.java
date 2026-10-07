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

        OrderResponse response = new OrderResponse();

        // ===================================
        // ORDER BASIC DETAILS
        // ===================================

        response.setId(order.getId());

        response.setAmount(order.getAmount());

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
        // CUSTOMER DETAILS
        // ===================================

        if (order.getUser() != null) {

            // User ID
            response.setUserId(
                    order.getUser().getId()
            );

            // Customer Email
            response.setCustomerEmail(
                    order.getUser().getEmail()
            );

            // Customer Full Name
            String firstName =
                    order.getUser().getFirstName();

            String lastName =
                    order.getUser().getLastName();

            String customerName = "";

            if (firstName != null) {
                customerName = firstName.trim();
            }

            if (lastName != null && !lastName.trim().isEmpty()) {

                if (!customerName.isEmpty()) {
                    customerName += " ";
                }

                customerName += lastName.trim();
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

        // Uncomment if Order entity contains deliveryDate
        // response.setDeliveryDate(order.getDeliveryDate());

        // ===================================
        // DELIVERY ADDRESS
        // ===================================

        if (order.getAddress() != null) {

            Address address = order.getAddress();

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

        if (order.getOrderItems() != null) {

            for (OrderItem item : order.getOrderItems()) {

                OrderItemResponse itemResponse =
                        new OrderItemResponse();

                // ===================================
                // PRODUCT ID
                // ===================================

                if (item.getProduct() != null) {

                    itemResponse.setProductId(
                            item.getProduct().getId()
                    );
                }

                // ===================================
                // PRODUCT DETAILS
                // ===================================

                itemResponse.setProductName(
                        item.getProductName()
                );

                itemResponse.setProductImage(
                        item.getProductImage()
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

                items.add(itemResponse);
            }
        }

        // ===================================
        // ORDER ITEMS RESPONSE
        // ===================================

        response.setItems(items);

        response.setTotalItems(
                items.size()
        );

        // ===================================
        // RETURN RESPONSE
        // ===================================

        return response;
    }
}