package com.example.manikantasales.serviceimpl;

import com.example.manikantasales.entity.Address;
import com.example.manikantasales.entity.Order;
import com.example.manikantasales.entity.OrderItem;
import com.example.manikantasales.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // =====================================================
    // NORMAL TEXT EMAIL
    // =====================================================

    @Override
    public void sendEmail(
            String to,
            String subject,
            String body
    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    // =====================================================
    // OTP EMAIL
    // =====================================================

    @Override
    public void sendOtpEmail(
            String email,
            String otp
    ) {

        sendEmail(
                email,
                "Manikanta Sales - OTP Verification",

                """
                Hello,

                Welcome to Manikanta Sales.

                Your OTP verification code is:

                %s

                This OTP is valid for 5 minutes.

                Do not share this OTP with anyone.

                Regards,

                Manikanta Sales Team
                """.formatted(otp)
        );
    }

    // =====================================================
    // WELCOME EMAIL
    // =====================================================

    @Override
    public void sendWelcomeEmail(
            String email,
            String firstName
    ) {

        sendEmail(
                email,
                "Welcome to Manikanta Sales",

                """
                Hello %s,

                Your account has been verified successfully.

                Welcome to Manikanta Sales!

                Happy Shopping!

                Regards,

                Manikanta Sales Team
                """.formatted(firstName)
        );
    }

    // =====================================================
    // FORGOT PASSWORD OTP
    // =====================================================

    @Override
    public void sendForgotPasswordOtp(
            String email,
            String otp
    ) {

        sendEmail(
                email,
                "Manikanta Sales - Password Reset",

                """
                Hello,

                Your password reset OTP is:

                %s

                This OTP is valid for 5 minutes.

                Regards,

                Manikanta Sales Team
                """.formatted(otp)
        );
    }

    // =====================================================
    // ORDER CONFIRMATION HTML EMAIL
    // =====================================================

    @Override
    public void sendOrderConfirmationEmail(Order order) {

        System.out.println("Preparing Order Confirmation Email...");

        // =================================================
        // PRODUCT ROWS
        // =================================================

        StringBuilder productRows = new StringBuilder();

        if (order.getOrderItems() != null) {

            for (OrderItem item : order.getOrderItems()) {

                productRows.append(
                        """
                        <tr>

                            <td style="
                                padding:14px;
                                border-bottom:1px solid #e5e7eb;
                                color:#333333;
                                font-size:14px;
                            ">
                                %s
                            </td>

                            <td style="
                                padding:14px;
                                text-align:center;
                                border-bottom:1px solid #e5e7eb;
                                color:#333333;
                                font-size:14px;
                            ">
                                %s
                            </td>

                            <td style="
                                padding:14px;
                                text-align:right;
                                border-bottom:1px solid #e5e7eb;
                                color:#333333;
                                font-size:14px;
                            ">
                                ₹%s
                            </td>

                        </tr>
                        """.formatted(
                                item.getProductName(),
                                item.getQuantity(),
                                item.getSubTotal()
                        )
                );
            }
        }

        // =================================================
        // DELIVERY ADDRESS
        // =================================================

        String address = "Address not available";

        if (order.getAddress() != null) {

            Address orderAddress = order.getAddress();

            address =
                    "<strong>"
                    + orderAddress.getName()
                    + "</strong><br>"
                    + "Mobile: "
                    + orderAddress.getMobile()
                    + "<br>"
                    + orderAddress.getHouseNo()
                    + ", "
                    + orderAddress.getStreet()
                    + "<br>"
                    + orderAddress.getCity()
                    + ", "
                    + orderAddress.getState()
                    + " - "
                    + orderAddress.getPincode();
        }

        // =================================================
        // HTML EMAIL
        // =================================================

        String html =
                """
                <!DOCTYPE html>

                <html>

                <head>

                    <meta charset="UTF-8">

                    <meta name="viewport"
                          content="width=device-width,
                                   initial-scale=1.0">

                    <title>
                        Order Confirmed
                    </title>

                </head>

                <body style="
                    margin:0;
                    padding:0;
                    background:#f3f4f6;
                    font-family:Arial,Helvetica,sans-serif;
                ">

                <div style="
                    max-width:700px;
                    margin:30px auto;
                    background:#ffffff;
                    border-radius:14px;
                    overflow:hidden;
                    box-shadow:0 4px 15px
                               rgba(0,0,0,0.08);
                ">

                    <!-- HEADER -->

                    <div style="
                        background:#172554;
                        padding:25px;
                        text-align:center;
                    ">

                        <h1 style="
                            margin:0;
                            color:#f59e0b;
                            font-size:28px;
                        ">

                            Manikanta Sales

                        </h1>

                        <p style="
                            margin:7px 0 0;
                            color:#dbeafe;
                            font-size:13px;
                        ">

                            Your trusted online shopping store

                        </p>

                    </div>


                    <!-- CONTENT -->

                    <div style="
                        padding:30px;
                    ">


                        <!-- SUCCESS BOX -->

                        <div style="
                            background:#dcfce7;
                            border:1px solid #86efac;
                            padding:20px;
                            border-radius:10px;
                            text-align:center;
                        ">

                            <div style="
                                font-size:36px;
                                color:#16a34a;
                            ">

                                ✓

                            </div>

                            <h2 style="
                                margin:8px 0;
                                color:#166534;
                            ">

                                Order Confirmed!

                            </h2>

                            <p style="
                                margin:0;
                                color:#166534;
                            ">

                                Your order has been placed successfully.

                            </p>

                        </div>


                        <!-- GREETING -->

                        <p style="
                            margin-top:25px;
                            color:#333333;
                            font-size:15px;
                        ">

                            Hello

                            <b>
                                %s
                            </b>,

                        </p>


                        <p style="
                            color:#555555;
                            line-height:1.6;
                        ">

                            Thank you for shopping with

                            <b>
                                Manikanta Sales
                            </b>.

                            Your order has been received
                            successfully and is now being processed.

                        </p>


                        <!-- ORDER INFORMATION -->

                        <div style="
                            background:#f8fafc;
                            border:1px solid #e5e7eb;
                            border-radius:10px;
                            padding:18px;
                            margin:22px 0;
                        ">

                            <p style="
                                margin:6px 0;
                                color:#333333;
                            ">

                                <b>Order ID:</b>

                                #%s

                            </p>

                            <p style="
                                margin:6px 0;
                                color:#333333;
                            ">

                                <b>Order Status:</b>

                                %s

                            </p>

                            <p style="
                                margin:6px 0;
                                color:#333333;
                            ">

                                <b>Payment Status:</b>

                                %s

                            </p>

                        </div>


                        <!-- ORDER ITEMS -->

                        <h3 style="
                            color:#172554;
                            margin-bottom:12px;
                        ">

                            Order Items

                        </h3>


                        <table
                            width="100%%"
                            cellpadding="0"
                            cellspacing="0"
                            style="
                                border-collapse:collapse;
                                border:1px solid #e5e7eb;
                            "
                        >

                            <tr style="
                                background:#172554;
                                color:#ffffff;
                            ">

                                <th style="
                                    padding:13px;
                                    text-align:left;
                                    font-size:14px;
                                ">

                                    Product

                                </th>

                                <th style="
                                    padding:13px;
                                    text-align:center;
                                    font-size:14px;
                                ">

                                    Qty

                                </th>

                                <th style="
                                    padding:13px;
                                    text-align:right;
                                    font-size:14px;
                                ">

                                    Amount

                                </th>

                            </tr>

                            %s

                        </table>


                        <!-- TOTAL -->

                        <div style="
                            text-align:right;
                            margin-top:20px;
                            padding:18px;
                            background:#fff7ed;
                            border-radius:10px;
                        ">

                            <span style="
                                color:#555555;
                                font-size:15px;
                            ">

                                Total Amount

                            </span>

                            <div style="
                                color:#ea580c;
                                font-size:24px;
                                font-weight:bold;
                                margin-top:5px;
                            ">

                                ₹%s

                            </div>

                        </div>


                        <!-- DELIVERY ADDRESS -->

                        <h3 style="
                            color:#172554;
                            margin-top:25px;
                        ">

                            Delivery Address

                        </h3>


                        <div style="
                            background:#f8fafc;
                            border:1px solid #e5e7eb;
                            padding:15px;
                            border-radius:8px;
                            color:#555555;
                            line-height:1.7;
                        ">

                            %s

                        </div>


                        <!-- FOOTER -->

                        <div style="
                            margin-top:28px;
                            padding:20px;
                            background:#172554;
                            color:#ffffff;
                            text-align:center;
                            border-radius:10px;
                        ">

                            Thank you for shopping with

                            <b style="
                                color:#f59e0b;
                            ">

                                Manikanta Sales

                            </b>

                            ❤️

                            <br>

                            <span style="
                                display:block;
                                margin-top:8px;
                                color:#dbeafe;
                                font-size:12px;
                            ">

                                We hope to serve you again!

                            </span>

                        </div>

                    </div>

                </div>

                </body>

                </html>

                """.formatted(

                        order.getUser().getFirstName(),
                        order.getId(),
                        order.getOrderStatus(),
                        order.getPaymentStatus(),
                        productRows.toString(),
                        order.getAmount(),
                        address
                );

        // =================================================
        // SEND EMAIL
        // =================================================

        sendHtmlEmail(
                order.getUser().getEmail(),
                "Order Confirmed #" + order.getId(),
                html
        );
    }

    // =====================================================
    // CANCEL ORDER HTML EMAIL
    // =====================================================

    @Override
    public void sendOrderCancellationEmail(Order order) {

        String html =
                """
                <!DOCTYPE html>

                <html>

                <body style="
                    margin:0;
                    padding:0;
                    background:#f3f4f6;
                    font-family:Arial,Helvetica,sans-serif;
                ">

                <div style="
                    max-width:650px;
                    margin:30px auto;
                    background:#ffffff;
                    border-radius:14px;
                    overflow:hidden;
                    box-shadow:0 4px 15px
                               rgba(0,0,0,0.08);
                ">

                    <!-- HEADER -->

                    <div style="
                        background:#172554;
                        padding:24px;
                        text-align:center;
                    ">

                        <h1 style="
                            margin:0;
                            color:#f59e0b;
                            font-size:28px;
                        ">

                            Manikanta Sales

                        </h1>

                        <p style="
                            color:#dbeafe;
                            margin:6px 0 0;
                        ">

                            Order Update

                        </p>

                    </div>


                    <!-- CONTENT -->

                    <div style="
                        padding:30px;
                    ">


                        <!-- CANCELLED BOX -->

                        <div style="
                            background:#fee2e2;
                            border:1px solid #fca5a5;
                            padding:20px;
                            border-radius:10px;
                            text-align:center;
                        ">

                            <div style="
                                font-size:38px;
                                color:#dc2626;
                            ">

                                ✕

                            </div>

                            <h2 style="
                                color:#b91c1c;
                                margin:8px 0;
                            ">

                                Order Cancelled

                            </h2>

                            <p style="
                                color:#991b1b;
                                margin:0;
                            ">

                                Your order has been cancelled successfully.

                            </p>

                        </div>


                        <p style="
                            margin-top:25px;
                            color:#333333;
                        ">

                            Hello

                            <b>
                                %s
                            </b>,

                        </p>


                        <p style="
                            color:#555555;
                            line-height:1.6;
                        ">

                            Your order cancellation has been
                            processed successfully.

                        </p>


                        <!-- ORDER DETAILS -->

                        <div style="
                            margin-top:22px;
                            background:#f8fafc;
                            border:1px solid #e5e7eb;
                            border-radius:10px;
                            padding:20px;
                        ">

                            <p style="margin:8px 0;">

                                <b>Order ID:</b>

                                #%s

                            </p>

                            <p style="margin:8px 0;">

                                <b>Order Amount:</b>

                                ₹%s

                            </p>

                            <p style="margin:8px 0;">

                                <b>Status:</b>

                                <span style="
                                    display:inline-block;
                                    margin-left:6px;
                                    padding:5px 10px;
                                    background:#fee2e2;
                                    color:#b91c1c;
                                    border-radius:20px;
                                    font-weight:bold;
                                ">

                                    CANCELLED

                                </span>

                            </p>

                        </div>


                        <!-- WARNING -->

                        <div style="
                            margin-top:25px;
                            padding:18px;
                            background:#fff7ed;
                            border-radius:10px;
                            color:#92400e;
                            line-height:1.6;
                        ">

                            If you did not request this cancellation,
                            please contact Manikanta Sales support.

                        </div>


                        <!-- FOOTER -->

                        <div style="
                            margin-top:28px;
                            padding:18px;
                            background:#172554;
                            color:#ffffff;
                            text-align:center;
                            border-radius:10px;
                        ">

                            Thank you for shopping with

                            <b style="
                                color:#f59e0b;
                            ">

                                Manikanta Sales

                            </b>

                        </div>

                    </div>

                </div>

                </body>

                </html>

                """.formatted(

                        order.getUser().getFirstName(),
                        order.getId(),
                        order.getAmount()
                );

        sendHtmlEmail(
                order.getUser().getEmail(),
                "Order Cancelled #" + order.getId(),
                html
        );
    }

    // =====================================================
    // RETURN REQUEST HTML EMAIL
    // =====================================================

    @Override
    public void sendReturnRequestEmail(Order order) {

        String html =
                """
                <!DOCTYPE html>

                <html>

                <body style="
                    margin:0;
                    padding:0;
                    background:#f3f4f6;
                    font-family:Arial,Helvetica,sans-serif;
                ">

                <div style="
                    max-width:650px;
                    margin:30px auto;
                    background:#ffffff;
                    border-radius:14px;
                    overflow:hidden;
                    box-shadow:0 4px 15px
                               rgba(0,0,0,0.08);
                ">

                    <!-- HEADER -->

                    <div style="
                        background:#172554;
                        padding:24px;
                        text-align:center;
                    ">

                        <h1 style="
                            margin:0;
                            color:#f59e0b;
                            font-size:28px;
                        ">

                            Manikanta Sales

                        </h1>

                        <p style="
                            color:#dbeafe;
                            margin:6px 0 0;
                        ">

                            Return Request

                        </p>

                    </div>


                    <!-- CONTENT -->

                    <div style="
                        padding:30px;
                    ">


                        <!-- RETURN BOX -->

                        <div style="
                            background:#fef3c7;
                            border:1px solid #fcd34d;
                            padding:20px;
                            border-radius:10px;
                            text-align:center;
                        ">

                            <div style="
                                font-size:38px;
                            ">

                                ↩

                            </div>

                            <h2 style="
                                color:#92400e;
                                margin:8px 0;
                            ">

                                Return Request Submitted

                            </h2>

                            <p style="
                                color:#92400e;
                                margin:0;
                            ">

                                We have received your return request.

                            </p>

                        </div>


                        <p style="
                            margin-top:25px;
                            color:#333333;
                        ">

                            Hello

                            <b>
                                %s
                            </b>,

                        </p>


                        <p style="
                            color:#555555;
                            line-height:1.6;
                        ">

                            Your return request has been successfully
                            submitted.

                            Our team will review your request and
                            update the return status.

                        </p>


                        <!-- ORDER DETAILS -->

                        <div style="
                            margin-top:22px;
                            background:#f8fafc;
                            border:1px solid #e5e7eb;
                            border-radius:10px;
                            padding:20px;
                        ">

                            <p style="margin:8px 0;">

                                <b>Order ID:</b>

                                #%s

                            </p>

                            <p style="margin:8px 0;">

                                <b>Order Amount:</b>

                                ₹%s

                            </p>

                            <p style="margin:8px 0;">

                                <b>Order Status:</b>

                                %s

                            </p>

                            <p style="margin:8px 0;">

                                <b>Return Status:</b>

                                <span style="
                                    display:inline-block;
                                    margin-left:6px;
                                    padding:5px 10px;
                                    background:#fef3c7;
                                    color:#92400e;
                                    border-radius:20px;
                                    font-weight:bold;
                                ">

                                    REQUESTED

                                </span>

                            </p>

                        </div>


                        <!-- FOOTER -->

                        <div style="
                            margin-top:28px;
                            padding:18px;
                            background:#172554;
                            color:#ffffff;
                            text-align:center;
                            border-radius:10px;
                        ">

                            We will keep you updated.

                            <br>
                            <br>

                            <b style="
                                color:#f59e0b;
                            ">

                                Manikanta Sales

                            </b>

                        </div>

                    </div>

                </div>

                </body>

                </html>

                """.formatted(

                        order.getUser().getFirstName(),
                        order.getId(),
                        order.getAmount(),
                        order.getOrderStatus()
                );

        sendHtmlEmail(
                order.getUser().getEmail(),
                "Return Request Submitted #"
                        + order.getId(),
                html
        );
    }

    // =====================================================
    // COMMON HTML EMAIL SENDER
    // =====================================================

    private void sendHtmlEmail(
            String to,
            String subject,
            String html
    ) {

        try {

            System.out.println("=================================");
            System.out.println("HTML EMAIL SENDING");
            System.out.println("TO : " + to);
            System.out.println("SUBJECT : " + subject);

            MimeMessage message =
                    mailSender.createMimeMessage();

            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true
                    );

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(
                    html,
                    true
            );

            mailSender.send(message);

            System.out.println(
                    "HTML EMAIL SENT SUCCESSFULLY"
            );

            System.out.println(
                    "================================="
            );

        } catch (MessagingException e) {

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "HTML EMAIL FAILED"
            );

            System.out.println(
                    "TO : " + to
            );

            System.out.println(
                    "SUBJECT : " + subject
            );

            e.printStackTrace();

            System.out.println(
                    "================================="
            );

            throw new RuntimeException(
                    "HTML email sending failed",
                    e
            );

        } catch (Exception e) {

            System.out.println(
                    "================================="
            );

            System.out.println(
                    "EMAIL SENDING FAILED"
            );

            System.out.println(
                    "TO : " + to
            );

            System.out.println(
                    "SUBJECT : " + subject
            );

            e.printStackTrace();

            System.out.println(
                    "================================="
            );

            throw new RuntimeException(
                    "Email sending failed",
                    e
            );
        }
    }
}