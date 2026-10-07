package com.example.manikantasales.controller;

import com.example.manikantasales.dto.OrderRequest;
import com.example.manikantasales.dto.OrderResponse;
import com.example.manikantasales.enums.OrderStatus;
import com.example.manikantasales.enums.PaymentStatus;
import com.example.manikantasales.service.OrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {

        this.orderService = orderService;

    }


    // =====================================================
    // USER
    // PLACE ORDER
    // =====================================================

    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(
            @RequestBody OrderRequest request
    ) {

        return ResponseEntity.ok(
                orderService.placeOrder(request)
        );

    }


    // =====================================================
    // USER
    // MY ORDERS
    // =====================================================

    @GetMapping("/my")
    public ResponseEntity<List<OrderResponse>> getMyOrders() {

        return ResponseEntity.ok(
                orderService.getMyOrders()
        );

    }


    // =====================================================
    // USER
    // MY ORDER BY ID
    // =====================================================

    @GetMapping("/my/{id}")
    public ResponseEntity<OrderResponse> getMyOrderById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                orderService.getMyOrderById(id)
        );

    }


    // =====================================================
    // USER
    // ORDER SUCCESS PAGE
    // GET SINGLE ORDER
    // =====================================================

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderDetails(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                orderService.getMyOrderById(id)
        );

    }


    // =====================================================
    // USER
    // CANCEL ORDER
    // =====================================================

    @PutMapping("/cancel/{id}")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                orderService.cancelOrder(id)
        );

    }


    // =====================================================
    // ADMIN
    // GET ALL ORDERS
    // =====================================================

    @GetMapping("/admin/all")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {

        return ResponseEntity.ok(
                orderService.getAllOrders()
        );

    }


    // =====================================================
    // ADMIN
    // GET ORDER BY ID
    // =====================================================

    @GetMapping("/admin/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                orderService.getOrderById(id)
        );

    }


    // =====================================================
    // ADMIN
    // UPDATE ORDER STATUS
    // =====================================================

    @PutMapping("/admin/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status
    ) {

        return ResponseEntity.ok(
                orderService.updateOrderStatus(
                        id,
                        status
                )
        );

    }


    // =====================================================
    // ADMIN
    // UPDATE PAYMENT STATUS
    // =====================================================

    @PutMapping("/admin/{id}/payment")
    public ResponseEntity<OrderResponse> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam PaymentStatus status
    ) {

        return ResponseEntity.ok(
                orderService.updatePaymentStatus(
                        id,
                        status
                )
        );

    }


    // =====================================================
    // ADMIN
    // DELETE ORDER
    // =====================================================

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<String> deleteOrder(
            @PathVariable Long id
    ) {

        orderService.deleteOrder(id);

        return ResponseEntity.ok(
                "Order deleted successfully"
        );

    }


    // =====================================================
    // DASHBOARD
    // REVENUE CARDS
    // =====================================================

    @GetMapping("/revenue")
    public ResponseEntity<?> getRevenue() {

        Map<String, Object> response =
                new HashMap<>();


        BigDecimal totalRevenue =
                orderService.getTotalRevenue();


        Long totalOrders =
                orderService.getOrderCount();


        BigDecimal averageOrder =
                BigDecimal.ZERO;


        if (totalOrders > 0) {

            averageOrder =
                    totalRevenue.divide(
                            BigDecimal.valueOf(totalOrders),
                            2,
                            RoundingMode.HALF_UP
                    );

        }


        response.put(
                "totalRevenue",
                totalRevenue
        );


        response.put(
                "todayRevenue",
                orderService.getTodayRevenue()
        );


        response.put(
                "monthRevenue",
                orderService.getMonthRevenue()
        );


        response.put(
                "totalOrders",
                totalOrders
        );


        response.put(
                "averageOrderValue",
                averageOrder
        );


        return ResponseEntity.ok(response);

    }


    // =====================================================
    // DASHBOARD
    // ORDER COUNT
    // =====================================================

    @GetMapping("/count")
    public ResponseEntity<Long> getOrderCount() {

        return ResponseEntity.ok(
                orderService.getOrderCount()
        );

    }


    // =====================================================
    // DASHBOARD
    // RECENT ORDERS
    // =====================================================

    @GetMapping("/recent")
    public ResponseEntity<List<OrderResponse>> getRecentOrders() {

        return ResponseEntity.ok(
                orderService.getRecentOrders()
        );

    }


    // =====================================================
    // DASHBOARD
    // REVENUE CHART
    // =====================================================

    @GetMapping("/chart")
    public ResponseEntity<?> revenueChart(

            @RequestParam(defaultValue = "daily")
            String type

    ) {

        return ResponseEntity.ok(
                orderService.getRevenueChart(type)
        );

    }


    // =====================================================
    // USER
    // REQUEST RETURN
    // =====================================================

    @PutMapping("/return/{orderId}")
    public ResponseEntity<OrderResponse> requestReturn(

            @PathVariable Long orderId,

            @RequestBody Map<String, String> request

    ) {

        String returnReason =
                request.get("returnReason");


        return ResponseEntity.ok(
                orderService.requestReturn(
                        orderId,
                        returnReason
                )
        );

    }


    // =====================================================
    // ADMIN
    // APPROVE RETURN
    // =====================================================

    @PutMapping("/return/{orderId}/approve")
    public ResponseEntity<OrderResponse> approveReturn(

            @PathVariable Long orderId

    ) {

        return ResponseEntity.ok(
                orderService.approveReturn(orderId)
        );

    }


    // =====================================================
    // ADMIN
    // REJECT RETURN
    // =====================================================

    @PutMapping("/return/{orderId}/reject")
    public ResponseEntity<OrderResponse> rejectReturn(

            @PathVariable Long orderId

    ) {

        return ResponseEntity.ok(
                orderService.rejectReturn(orderId)
        );

    }


    // =====================================================
    // ADMIN
    // COMPLETE RETURN
    // =====================================================

    @PutMapping("/return/{orderId}/complete")
    public ResponseEntity<OrderResponse> completeReturn(

            @PathVariable Long orderId

    ) {

        return ResponseEntity.ok(
                orderService.completeReturn(orderId)
        );

    }

}