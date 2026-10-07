package com.example.manikantasales.dto;

import java.math.BigDecimal;

public class DashboardResponse {

    private long totalProducts;
    private long totalCategories;
    private long totalUsers;
    private long totalOrders;
    private BigDecimal totalRevenue;

    public DashboardResponse() {
    }

    public DashboardResponse(long totalProducts,
                             long totalCategories,
                             long totalUsers,
                             long totalOrders,
                             BigDecimal totalRevenue) {
        this.totalProducts = totalProducts;
        this.totalCategories = totalCategories;
        this.totalUsers = totalUsers;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getTotalCategories() {
        return totalCategories;
    }

    public void setTotalCategories(long totalCategories) {
        this.totalCategories = totalCategories;
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}