package com.example.manikantasales.repository;

import com.example.manikantasales.entity.Order;
import com.example.manikantasales.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // =====================================================
    // USER ORDERS
    // =====================================================

    List<Order> findByUserOrderByCreatedDateDesc(
            User user
    );

    // =====================================================
    // USER SINGLE ORDER
    // =====================================================

    Optional<Order> findByIdAndUser(
            Long id,
            User user
    );

    // =====================================================
    // TOTAL REVENUE
    // =====================================================

    @Query("""
            SELECT COALESCE(
                SUM(o.amount),
                0
            )
            FROM Order o
            """)
    BigDecimal getTotalRevenue();

    // =====================================================
    // TODAY REVENUE
    // =====================================================

    @Query("""
            SELECT COALESCE(
                SUM(o.amount),
                0
            )
            FROM Order o
            WHERE FUNCTION('DATE', o.createdDate)
                  = CURRENT_DATE
            """)
    BigDecimal getTodayRevenue();

    // =====================================================
    // MONTH REVENUE
    // =====================================================

    @Query("""
            SELECT COALESCE(
                SUM(o.amount),
                0
            )
            FROM Order o
            WHERE FUNCTION('MONTH', o.createdDate)
                  =
                  FUNCTION('MONTH', CURRENT_DATE)

            AND FUNCTION('YEAR', o.createdDate)
                  =
                  FUNCTION('YEAR', CURRENT_DATE)
            """)
    BigDecimal getMonthRevenue();

    // =====================================================
    // DAILY SALES GRAPH
    // =====================================================

    @Query("""
            SELECT
                FUNCTION('DATE', o.createdDate),
                SUM(o.amount)

            FROM Order o

            GROUP BY FUNCTION('DATE', o.createdDate)

            ORDER BY FUNCTION('DATE', o.createdDate)
            """)
    List<Object[]> dailyRevenue();

    // =====================================================
    // WEEKLY SALES GRAPH
    // =====================================================

    @Query("""
            SELECT
                FUNCTION('WEEK', o.createdDate),
                SUM(o.amount)

            FROM Order o

            GROUP BY FUNCTION('WEEK', o.createdDate)

            ORDER BY FUNCTION('WEEK', o.createdDate)
            """)
    List<Object[]> weeklyRevenue();

    // =====================================================
    // MONTHLY SALES GRAPH
    // =====================================================

    @Query("""
            SELECT
                FUNCTION('MONTH', o.createdDate),
                SUM(o.amount)

            FROM Order o

            GROUP BY FUNCTION('MONTH', o.createdDate)

            ORDER BY FUNCTION('MONTH', o.createdDate)
            """)
    List<Object[]> monthlyRevenue();

    // =====================================================
    // RECENT ORDERS
    // =====================================================

    @Query("""
            SELECT o
            FROM Order o
            ORDER BY o.id DESC
            """)
    List<Order> findRecentOrders();
}