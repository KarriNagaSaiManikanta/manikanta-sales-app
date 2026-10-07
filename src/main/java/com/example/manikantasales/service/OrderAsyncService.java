package com.example.manikantasales.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class OrderAsyncService {

    private final EmailService emailService;

    public OrderAsyncService(EmailService emailService) {
        this.emailService = emailService;
    }

    // =====================================================
    // ORDER PLACE EMAIL
    // =====================================================

    @Async
    public void sendOrderEmails(
            String email,
            String firstName,
            Long orderId
    ) {

        System.out.println("=================================");
        System.out.println("ORDER EMAIL STARTED");
        System.out.println("Order ID : " + orderId);
        System.out.println("Customer Email : " + email);

        try {

            String subject =
                    "Order Confirmed - Manikanta Sales";

            String body =
                    "Hello "
                            + firstName
                            + ",\n\n"

                            + "Your order has been placed successfully."

                            + "\n\nOrder ID : "
                            + orderId

                            + "\n\nThank you for shopping "
                            + "with Manikanta Sales.";

            emailService.sendEmail(
                    email,
                    subject,
                    body
            );

            System.out.println(
                    "ORDER CONFIRMATION EMAIL SENT SUCCESSFULLY"
            );

        } catch (Exception e) {

            System.out.println(
                    "ORDER CONFIRMATION EMAIL FAILED"
            );

            e.printStackTrace();
        }

        System.out.println(
                "ORDER EMAIL PROCESS COMPLETED"
        );

        System.out.println(
                "================================="
        );
    }

    // =====================================================
    // ORDER CANCEL EMAIL
    // =====================================================

    @Async
    public void sendOrderCancellationEmail(
            String email,
            String firstName,
            Long orderId
    ) {

        try {

            String subject =
                    "Order Cancelled - Manikanta Sales";

            String body =
                    "Hello "
                            + firstName
                            + ",\n\n"

                            + "Your order has been cancelled."

                            + "\n\nOrder ID : "
                            + orderId

                            + "\n\nThank you for shopping "
                            + "with Manikanta Sales.";

            emailService.sendEmail(
                    email,
                    subject,
                    body
            );

            System.out.println(
                    "ORDER CANCELLATION EMAIL SENT"
            );

        } catch (Exception e) {

            System.out.println(
                    "ORDER CANCELLATION EMAIL FAILED"
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // RETURN REQUEST EMAIL
    // =====================================================

    @Async
    public void sendReturnRequestEmail(
            String email,
            String firstName,
            Long orderId,
            String returnReason
    ) {

        try {

            String subject =
                    "Return Request Received - Manikanta Sales";

            String body =
                    "Hello "
                            + firstName
                            + ",\n\n"

                            + "Your return request has been received."

                            + "\n\nOrder ID : "
                            + orderId

                            + "\nReturn Reason : "
                            + returnReason

                            + "\nReturn Status : REQUESTED"

                            + "\n\nOur team will review your "
                            + "return request shortly."

                            + "\n\nThank you for shopping "
                            + "with Manikanta Sales.";

            emailService.sendEmail(
                    email,
                    subject,
                    body
            );

            System.out.println(
                    "RETURN REQUEST EMAIL SENT"
            );

        } catch (Exception e) {

            System.out.println(
                    "RETURN REQUEST EMAIL FAILED"
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // RETURN APPROVED EMAIL
    // =====================================================

    @Async
    public void sendReturnApprovedEmail(
            String email,
            String firstName,
            Long orderId,
            String amount,
            String returnStatus
    ) {

        try {

            String subject =
                    "Return Approved - Manikanta Sales";

            String body =
                    "Hello "
                            + firstName
                            + ",\n\n"

                            + "Good news! Your return request "
                            + "has been approved."

                            + "\n\nOrder ID : "
                            + orderId

                            + "\nOrder Amount : ₹"
                            + amount

                            + "\nReturn Status : "
                            + returnStatus

                            + "\n\nPlease follow the return "
                            + "instructions provided by "
                            + "Manikanta Sales."

                            + "\n\nThank you for shopping "
                            + "with Manikanta Sales.";

            emailService.sendEmail(
                    email,
                    subject,
                    body
            );

            System.out.println(
                    "RETURN APPROVED EMAIL SENT"
            );

        } catch (Exception e) {

            System.out.println(
                    "RETURN APPROVED EMAIL FAILED"
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // RETURN REJECTED EMAIL
    // =====================================================

    @Async
    public void sendReturnRejectedEmail(
            String email,
            String firstName,
            Long orderId,
            String amount,
            String returnStatus
    ) {

        try {

            String subject =
                    "Return Request Update - Manikanta Sales";

            String body =
                    "Hello "
                            + firstName
                            + ",\n\n"

                            + "We are sorry, but your return request "
                            + "could not be approved."

                            + "\n\nOrder ID : "
                            + orderId

                            + "\nOrder Amount : ₹"
                            + amount

                            + "\nReturn Status : "
                            + returnStatus

                            + "\n\nIf you have any questions, "
                            + "please contact Manikanta Sales support."

                            + "\n\nThank you for shopping "
                            + "with Manikanta Sales.";

            emailService.sendEmail(
                    email,
                    subject,
                    body
            );

            System.out.println(
                    "RETURN REJECTED EMAIL SENT"
            );

        } catch (Exception e) {

            System.out.println(
                    "RETURN REJECTED EMAIL FAILED"
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // RETURN COMPLETED EMAIL
    // =====================================================

    @Async
    public void sendReturnCompletedEmail(
            String email,
            String firstName,
            Long orderId,
            String amount,
            String returnStatus
    ) {

        try {

            String subject =
                    "Return Completed - Manikanta Sales";

            String body =
                    "Hello "
                            + firstName
                            + ",\n\n"

                            + "Your product return has been "
                            + "completed successfully."

                            + "\n\nOrder ID : "
                            + orderId

                            + "\nOrder Amount : ₹"
                            + amount

                            + "\nReturn Status : "
                            + returnStatus

                            + "\n\nThank you for shopping "
                            + "with Manikanta Sales.";

            emailService.sendEmail(
                    email,
                    subject,
                    body
            );

            System.out.println(
                    "RETURN COMPLETED EMAIL SENT"
            );

        } catch (Exception e) {

            System.out.println(
                    "RETURN COMPLETED EMAIL FAILED"
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // ORDER STATUS UPDATE EMAIL
    // =====================================================

    @Async
    public void sendOrderStatusEmail(
            String email,
            String firstName,
            Long orderId,
            String orderStatus
    ) {

        System.out.println("=================================");
        System.out.println("ORDER STATUS EMAIL STARTED");
        System.out.println("Order ID : " + orderId);
        System.out.println("Customer Email : " + email);

        try {

            String subject =
                    "Order Status Updated - Order #"
                            + orderId;

            String body =
                    "Hello "
                            + firstName
                            + ",\n\n"

                            + "Your order "
                            + orderId

                            + " status has been updated to "
                            + orderStatus

                            + ".\n\n"

                            + "Thank you for shopping "
                            + "with Manikanta Sales.";

            emailService.sendEmail(
                    email,
                    subject,
                    body
            );

            System.out.println(
                    "ORDER STATUS EMAIL SENT SUCCESSFULLY"
            );

        } catch (Exception e) {

            System.out.println(
                    "ORDER STATUS EMAIL FAILED"
            );

            e.printStackTrace();
        }

        System.out.println(
                "================================="
        );
    }
}