package com.example.manikantasales.service;

import com.example.manikantasales.dto.OrderRequest;
import com.example.manikantasales.dto.OrderResponse;
import com.example.manikantasales.entity.Order;
import com.example.manikantasales.enums.OrderStatus;
import com.example.manikantasales.enums.PaymentStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrderService {

    // ======================================================
    // USER ORDER
    // ======================================================

    // Place Order
    OrderResponse placeOrder(
            OrderRequest request
    );


    // My Orders
    List<OrderResponse> getMyOrders();


    // My Order Details
    OrderResponse getMyOrderById(
            Long orderId
    );


    // Cancel Order
    OrderResponse cancelOrder(
            Long orderId
    );


    // Return Order
    OrderResponse requestReturn(
            Long orderId,
            String returnReason
    );


    // ======================================================
    // ADMIN ORDER
    // ======================================================

    // Get All Orders
    List<OrderResponse> getAllOrders();


    // Get Order Details
    OrderResponse getOrderById(
            Long orderId
    );


    // Update Order Status
    OrderResponse updateOrderStatus(
            Long orderId,
            OrderStatus orderStatus
    );


    // Update Payment Status
    OrderResponse updatePaymentStatus(
            Long orderId,
            PaymentStatus paymentStatus
    );


    // Delete Order
    void deleteOrder(
            Long orderId
    );


    // ======================================================
    // ADMIN RETURN MANAGEMENT
    // ======================================================

    // Approve Return Request
    OrderResponse approveReturn(
            Long orderId
    );


    // Reject Return Request
    OrderResponse rejectReturn(
            Long orderId
    );


    // Complete Return
    OrderResponse completeReturn(
            Long orderId
    );


    // ======================================================
    // ADMIN DASHBOARD
    // ======================================================

    // Total Orders Count
    Long getOrderCount();


    // Total Revenue
    BigDecimal getTotalRevenue();


    // Today's Revenue
    BigDecimal getTodayRevenue();


    // Monthly Revenue
    BigDecimal getMonthRevenue();


    // ======================================================
    // RECENT ORDERS
    // ======================================================

    List<OrderResponse> getRecentOrders();


    // ======================================================
    // REVENUE GRAPH
    // ======================================================

    Map<String, Object> getRevenueChart(
            String type
    );


    // ======================================================
    // INTERNAL
    // ======================================================

    Order findOrderEntityById(
            Long orderId
    );

}