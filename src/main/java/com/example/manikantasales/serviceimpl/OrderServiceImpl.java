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
    // GET LOGGED-IN USER
    // =====================================================

    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
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
                                "User not found : " + email
                        )
                );
    }

    // =====================================================
    // PLACE ORDER
    // CART -> ORDER
    // =====================================================

    @Override
    @Transactional
    public OrderResponse placeOrder(
            OrderRequest request
    ) {

        User user = getLoggedInUser();

        Address address =
                addressRepository
                        .findById(request.getAddressId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Address not found"
                                )
                        );

        List<Cart> cartItems =
                cartRepository.findCartWithProduct(user);

        if (cartItems.isEmpty()) {

            throw new RuntimeException(
                    "Cart is empty"
            );
        }

        // -------------------------------------------------
        // CREATE ORDER
        // -------------------------------------------------

        Order order = new Order();

        order.setUser(user);

        order.setAddress(address);

        order.setOrderStatus(
                OrderStatus.PENDING
        );

        // -------------------------------------------------
        // PAYMENT STATUS
        // -------------------------------------------------
        // COD order starts as PENDING.
        //
        // Payment becomes PAID only after
        // order reaches DELIVERED.
        // -------------------------------------------------

        if (request.getPaymentMethod()
                == PaymentMethod.COD) {

            order.setPaymentStatus(
                    PaymentStatus.PENDING
            );

        } else {

            /*
             * Future online payment support.
             *
             * Currently application uses COD.
             * Keep online payment as PAID only if
             * a successful online payment is implemented.
             */

            order.setPaymentStatus(
                    PaymentStatus.PAID
            );
        }

        order.setPaymentMethod(
                request.getPaymentMethod()
        );

        // -------------------------------------------------
        // ORDER ITEMS
        // -------------------------------------------------

        List<OrderItem> orderItems =
                new ArrayList<>();

        List<Product> productsToUpdate =
                new ArrayList<>();

        BigDecimal totalAmount =
                BigDecimal.ZERO;

        for (Cart cart : cartItems) {

            Product product =
                    cart.getProduct();

            int quantity =
                    cart.getQuantity();

            // -------------------------------------------------
            // STOCK CHECK
            // -------------------------------------------------

            if (product.getQuantity() < quantity) {

                throw new RuntimeException(
                        product.getName()
                                + " stock unavailable"
                );
            }

            // -------------------------------------------------
            // SUB TOTAL
            // -------------------------------------------------

            BigDecimal subTotal =
                    product.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            quantity
                                    )
                            );

            totalAmount =
                    totalAmount.add(subTotal);

            // -------------------------------------------------
            // CREATE ORDER ITEM
            // -------------------------------------------------

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

            // -------------------------------------------------
            // REDUCE STOCK
            // -------------------------------------------------

            product.setQuantity(
                    product.getQuantity()
                            - quantity
            );

            productsToUpdate.add(
                    product
            );
        }

        // -------------------------------------------------
        // SHIPPING
        // -------------------------------------------------

        BigDecimal shipping =
                BigDecimal.valueOf(
                        request.getShippingCharge()
                );

        totalAmount =
                totalAmount.add(shipping);

        order.setAmount(
                totalAmount
        );

        order.setOrderItems(
                orderItems
        );

        // -------------------------------------------------
        // SAVE ORDER
        // -------------------------------------------------

        Order savedOrder =
                orderRepository.save(order);

        // -------------------------------------------------
        // UPDATE PRODUCT STOCK
        // -------------------------------------------------

        productRepository.saveAll(
                productsToUpdate
        );

        // -------------------------------------------------
        // CLEAR CART
        // -------------------------------------------------

        cartRepository.deleteByUser(user);

        // -------------------------------------------------
        // GET EMAIL VALUES BEFORE ASYNC
        // -------------------------------------------------

        String email =
                user.getEmail();

        String firstName =
                user.getFirstName();

        Long orderId =
                savedOrder.getId();

        // -------------------------------------------------
        // SEND ORDER EMAIL ASYNC
        // -------------------------------------------------

        orderAsyncService.sendOrderEmails(
                email,
                firstName,
                orderId
        );

        // -------------------------------------------------
        // RESPONSE
        // -------------------------------------------------

        return OrderMapper.toResponse(
                savedOrder
        );
    }

    // =====================================================
    // GET MY ORDERS
    // USER
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
    // GET MY ORDER BY ID
    // USER
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

        return OrderMapper.toResponse(
                order
        );
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

        // -------------------------------------------------
        // CHECK STATUS
        // -------------------------------------------------

        if (order.getOrderStatus()
                == OrderStatus.CANCELLED) {

            throw new RuntimeException(
                    "Order already cancelled"
            );
        }

        if (order.getOrderStatus()
                != OrderStatus.PENDING
                && order.getOrderStatus()
                != OrderStatus.CONFIRMED) {

            throw new RuntimeException(
                    "Only pending or confirmed orders can be cancelled"
            );
        }

        // -------------------------------------------------
        // RESTORE PRODUCT STOCK
        // -------------------------------------------------

        for (OrderItem item :
                order.getOrderItems()) {

            Product product =
                    item.getProduct();

            product.setQuantity(
                    product.getQuantity()
                            + item.getQuantity()
            );

            productRepository.save(
                    product
            );
        }

        // -------------------------------------------------
        // UPDATE ORDER STATUS
        // -------------------------------------------------

        order.setOrderStatus(
                OrderStatus.CANCELLED
        );

        /*
         * COD payment was not collected.
         *
         * Therefore payment status remains PENDING.
         */

        Order updatedOrder =
                orderRepository.save(order);

        // -------------------------------------------------
        // GET EMAIL VALUES
        // -------------------------------------------------

        String email =
                user.getEmail();

        String firstName =
                user.getFirstName();

        Long id =
                updatedOrder.getId();

        // -------------------------------------------------
        // CANCELLATION EMAIL
        // -------------------------------------------------

        orderAsyncService
                .sendOrderCancellationEmail(
                        email,
                        firstName,
                        id
                );

        // -------------------------------------------------
        // RESPONSE
        // -------------------------------------------------

        return OrderMapper.toResponse(
                updatedOrder
        );
    }

    // =====================================================
    // GET ALL ORDERS
    // ADMIN
    // =====================================================

    @Override
    public List<OrderResponse> getAllOrders() {

        List<Order> orders =
                orderRepository.findAll();

        return orders.stream()
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

        return OrderMapper.toResponse(
                order
        );
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

        // -------------------------------------------------
        // UPDATE ORDER STATUS
        // -------------------------------------------------

        order.setOrderStatus(
                orderStatus
        );

        // -------------------------------------------------
        // AUTOMATIC COD PAYMENT FLOW
        // -------------------------------------------------
        //
        // PENDING          -> PAYMENT PENDING
        // CONFIRMED        -> PAYMENT PENDING
        // PROCESSING       -> PAYMENT PENDING
        // SHIPPED          -> PAYMENT PENDING
        // OUT_FOR_DELIVERY -> PAYMENT PENDING
        // DELIVERED        -> PAYMENT PAID
        //
        // -------------------------------------------------

        if (orderStatus == OrderStatus.DELIVERED) {

            order.setPaymentStatus(
                    PaymentStatus.PAID
            );

        } else if (
                orderStatus == OrderStatus.PENDING
                        || orderStatus == OrderStatus.CONFIRMED
                        || orderStatus == OrderStatus.PROCESSING
                        || orderStatus == OrderStatus.SHIPPED
                        || orderStatus == OrderStatus.OUT_FOR_DELIVERY
        ) {

            order.setPaymentStatus(
                    PaymentStatus.PENDING
            );
        }

        // -------------------------------------------------
        // SAVE ORDER
        // -------------------------------------------------

        Order updatedOrder =
                orderRepository.save(order);

        // -------------------------------------------------
        // READ LAZY USER DATA
        // WHILE TRANSACTION IS ACTIVE
        // -------------------------------------------------

        String email =
                updatedOrder.getUser().getEmail();

        String firstName =
                updatedOrder.getUser().getFirstName();

        Long id =
                updatedOrder.getId();

        String status =
                updatedOrder
                        .getOrderStatus()
                        .name();

        // -------------------------------------------------
        // MAP RESPONSE BEFORE ASYNC
        // -------------------------------------------------

        OrderResponse response =
                OrderMapper.toResponse(
                        updatedOrder
                );

        // -------------------------------------------------
        // SEND EMAIL USING PLAIN VALUES
        // -------------------------------------------------

        orderAsyncService.sendOrderStatusEmail(
                email,
                firstName,
                id,
                status
        );

        // -------------------------------------------------
        // RETURN RESPONSE
        // -------------------------------------------------

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

        return OrderMapper.toResponse(
                updatedOrder
        );
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
    // FIND ORDER ENTITY BY ID
    // INTERNAL USE
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
    // ADMIN DASHBOARD
    // =====================================================

    @Override
    public Long getOrderCount() {

        return orderRepository.count();
    }

    // =====================================================
    // TOTAL REVENUE
    // ADMIN DASHBOARD
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
    // ADMIN DASHBOARD
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
    // ADMIN DASHBOARD
    // =====================================================

    @Override
    public BigDecimal getMonthRevenue() {

        BigDecimal revenue =
                orderRepository.getMonthRevenue();

        return revenue != null
                ? revenue
                : BigDecimal.ZERO;
    }

    // =====================================================
    // RECENT ORDERS
    // ADMIN DASHBOARD
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getRecentOrders() {

        List<Order> orders =
                orderRepository.findRecentOrders();

        return orders.stream()
                .limit(10)
                .map(OrderMapper::toResponse)
                .collect(Collectors.toList());
    }

    // =====================================================
    // REVENUE CHART
    // ADMIN DASHBOARD
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getRevenueChart(
            String type
    ) {

        Map<String, Object> response =
                new HashMap<>();

        List<String> labels =
                new ArrayList<>();

        List<BigDecimal> values =
                new ArrayList<>();

        // -------------------------------------------------
        // DAILY
        // -------------------------------------------------

        if ("daily".equalsIgnoreCase(type)) {

            List<Object[]> results =
                    orderRepository.dailyRevenue();

            for (Object[] row : results) {

                Object date =
                        row[0];

                Object amount =
                        row[1];

                labels.add(
                        String.valueOf(date)
                );

                values.add(
                        amount instanceof BigDecimal
                                ? (BigDecimal) amount
                                : new BigDecimal(
                                        String.valueOf(amount)
                                )
                );
            }
        }

        // -------------------------------------------------
        // WEEKLY
        // -------------------------------------------------

        else if ("weekly".equalsIgnoreCase(type)) {

            List<Object[]> results =
                    orderRepository.weeklyRevenue();

            for (Object[] row : results) {

                Object week =
                        row[0];

                Object amount =
                        row[1];

                labels.add(
                        "Week "
                                + String.valueOf(week)
                );

                values.add(
                        amount instanceof BigDecimal
                                ? (BigDecimal) amount
                                : new BigDecimal(
                                        String.valueOf(amount)
                                )
                );
            }
        }

        // -------------------------------------------------
        // MONTHLY
        // -------------------------------------------------

        else if ("monthly".equalsIgnoreCase(type)) {

            List<Object[]> results =
                    orderRepository.monthlyRevenue();

            for (Object[] row : results) {

                Object month =
                        row[0];

                Object amount =
                        row[1];

                labels.add(
                        "Month "
                                + String.valueOf(month)
                );

                values.add(
                        amount instanceof BigDecimal
                                ? (BigDecimal) amount
                                : new BigDecimal(
                                        String.valueOf(amount)
                                )
                );
            }
        }

        // -------------------------------------------------
        // INVALID TYPE
        // -------------------------------------------------

        else {

            throw new IllegalArgumentException(
                    "Invalid chart type. Use daily, weekly or monthly."
            );
        }

        // -------------------------------------------------
        // RESPONSE
        // -------------------------------------------------

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

    // =====================================================
    // REQUEST RETURN
    // USER
    // =====================================================

    @Override
    public OrderResponse requestReturn(
            Long orderId,
            String returnReason
    ) {

        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Order not found"
                                )
                        );

        // -------------------------------------------------
        // ONLY DELIVERED ORDERS CAN BE RETURNED
        // -------------------------------------------------

        if (order.getOrderStatus()
                != OrderStatus.DELIVERED) {

            throw new RuntimeException(
                    "Only delivered orders can be returned"
            );
        }

        // -------------------------------------------------
        // CHECK EXISTING RETURN
        // -------------------------------------------------

        if (order.getReturnStatus() != null
                && order.getReturnStatus()
                != ReturnStatus.NONE) {

            throw new RuntimeException(
                    "Return request already exists"
            );
        }

        // -------------------------------------------------
        // VALIDATE RETURN REASON
        // -------------------------------------------------

        if (returnReason == null
                || returnReason.trim().isEmpty()) {

            throw new RuntimeException(
                    "Return reason is required"
            );
        }

        // -------------------------------------------------
        // SAVE RETURN DETAILS
        // -------------------------------------------------

        order.setReturnStatus(
                ReturnStatus.REQUESTED
        );

        order.setReturnReason(
                returnReason.trim()
        );

        // -------------------------------------------------
        // PAYMENT REMAINS PAID WHILE RETURN IS REQUESTED
        // -------------------------------------------------

        order.setPaymentStatus(
                PaymentStatus.PAID
        );

        // -------------------------------------------------
        // SAVE ORDER
        // -------------------------------------------------

        Order updatedOrder =
                orderRepository.save(order);

        // -------------------------------------------------
        // GET EMAIL VALUES
        // -------------------------------------------------

        String email =
                updatedOrder
                        .getUser()
                        .getEmail();

        String firstName =
                updatedOrder
                        .getUser()
                        .getFirstName();

        Long id =
                updatedOrder.getId();

        String reason =
                updatedOrder.getReturnReason();

        // -------------------------------------------------
        // MAP RESPONSE BEFORE ASYNC
        // -------------------------------------------------

        OrderResponse response =
                OrderMapper.toResponse(
                        updatedOrder
                );

        // -------------------------------------------------
        // SEND RETURN EMAIL
        // -------------------------------------------------

        orderAsyncService.sendReturnRequestEmail(
                email,
                firstName,
                id,
                reason
        );

        return response;
    }

    // =====================================================
    // ADMIN - APPROVE RETURN
    // =====================================================

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

        if (order.getReturnStatus()
                != ReturnStatus.REQUESTED) {

            throw new RuntimeException(
                    "Return request is not pending"
            );
        }

        order.setReturnStatus(
                ReturnStatus.APPROVED
        );

        /*
         * Payment is still PAID until the return
         * is actually completed/refunded.
         */

        order.setPaymentStatus(
                PaymentStatus.PAID
        );

        Order updatedOrder =
                orderRepository.save(order);

        // -------------------------------------------------
        // EMAIL VALUES
        // -------------------------------------------------

        String email =
                updatedOrder
                        .getUser()
                        .getEmail();

        String firstName =
                updatedOrder
                        .getUser()
                        .getFirstName();

        Long id =
                updatedOrder.getId();

        String amount =
                updatedOrder
                        .getAmount()
                        .toString();

        String returnStatus =
                updatedOrder
                        .getReturnStatus()
                        .name();

        // -------------------------------------------------
        // MAP RESPONSE
        // -------------------------------------------------

        OrderResponse response =
                OrderMapper.toResponse(
                        updatedOrder
                );

        // -------------------------------------------------
        // APPROVED EMAIL
        // -------------------------------------------------

        orderAsyncService.sendReturnApprovedEmail(
                email,
                firstName,
                id,
                amount,
                returnStatus
        );

        return response;
    }

    // =====================================================
    // ADMIN - REJECT RETURN
    // =====================================================

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

        if (order.getReturnStatus()
                != ReturnStatus.REQUESTED) {

            throw new RuntimeException(
                    "Return request is not pending"
            );
        }

        order.setReturnStatus(
                ReturnStatus.REJECTED
        );

        /*
         * Return rejected means payment remains PAID.
         */

        order.setPaymentStatus(
                PaymentStatus.PAID
        );

        Order updatedOrder =
                orderRepository.save(order);

        // -------------------------------------------------
        // EMAIL VALUES
        // -------------------------------------------------

        String email =
                updatedOrder
                        .getUser()
                        .getEmail();

        String firstName =
                updatedOrder
                        .getUser()
                        .getFirstName();

        Long id =
                updatedOrder.getId();

        String amount =
                updatedOrder
                        .getAmount()
                        .toString();

        String returnStatus =
                updatedOrder
                        .getReturnStatus()
                        .name();

        // -------------------------------------------------
        // MAP RESPONSE
        // -------------------------------------------------

        OrderResponse response =
                OrderMapper.toResponse(
                        updatedOrder
                );

        // -------------------------------------------------
        // REJECTED EMAIL
        // -------------------------------------------------

        orderAsyncService.sendReturnRejectedEmail(
                email,
                firstName,
                id,
                amount,
                returnStatus
        );

        return response;
    }

    // =====================================================
    // ADMIN - COMPLETE RETURN
    // =====================================================

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

        // -------------------------------------------------
        // RETURN MUST BE APPROVED
        // -------------------------------------------------

        if (order.getReturnStatus()
                != ReturnStatus.APPROVED) {

            throw new RuntimeException(
                    "Return must be approved before completion"
            );
        }

        // -------------------------------------------------
        // COMPLETE RETURN
        // -------------------------------------------------

        order.setReturnStatus(
                ReturnStatus.RETURNED
        );

        // -------------------------------------------------
        // PAYMENT REFUNDED
        // -------------------------------------------------

        order.setPaymentStatus(
                PaymentStatus.REFUNDED
        );

        // -------------------------------------------------
        // SAVE ORDER
        // -------------------------------------------------

        Order updatedOrder =
                orderRepository.save(order);

        // -------------------------------------------------
        // EMAIL VALUES
        // -------------------------------------------------

        String email =
                updatedOrder
                        .getUser()
                        .getEmail();

        String firstName =
                updatedOrder
                        .getUser()
                        .getFirstName();

        Long id =
                updatedOrder.getId();

        String amount =
                updatedOrder
                        .getAmount()
                        .toString();

        String returnStatus =
                updatedOrder
                        .getReturnStatus()
                        .name();

        // -------------------------------------------------
        // MAP RESPONSE
        // -------------------------------------------------

        OrderResponse response =
                OrderMapper.toResponse(
                        updatedOrder
                );

        // -------------------------------------------------
        // COMPLETED EMAIL
        // -------------------------------------------------

        orderAsyncService.sendReturnCompletedEmail(
                email,
                firstName,
                id,
                amount,
                returnStatus
        );

        return response;
    }
}