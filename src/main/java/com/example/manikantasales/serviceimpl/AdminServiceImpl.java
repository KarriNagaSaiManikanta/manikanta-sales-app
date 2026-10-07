package com.example.manikantasales.serviceimpl;

import com.example.manikantasales.dto.DashboardResponse;
import com.example.manikantasales.repository.CategoryRepository;
import com.example.manikantasales.repository.OrderRepository;
import com.example.manikantasales.repository.ProductRepository;
import com.example.manikantasales.repository.UserRepository;
import com.example.manikantasales.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public DashboardResponse getDashboard() {

        long totalProducts = productRepository.count();
        long totalCategories = categoryRepository.count();
        long totalUsers = userRepository.count();
        long totalOrders = orderRepository.count();

        
        BigDecimal totalRevenue = BigDecimal.ZERO;

        return new DashboardResponse(
                totalProducts,
                totalCategories,
                totalUsers,
                totalOrders,
                totalRevenue
        );
    }
}