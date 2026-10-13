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


        try {


            System.out.println("=================================");
            System.out.println("TEXT EMAIL SENDING");

            System.out.println("TO : " + to);
            System.out.println("SUBJECT : " + subject);


            System.out.println(
                    "MAIL_USERNAME = "
                    + System.getenv("MAIL_USERNAME")
            );


            System.out.println(
                    "MAIL_PASSWORD = "
                    + System.getenv("MAIL_PASSWORD")
            );



            SimpleMailMessage message =
                    new SimpleMailMessage();



            message.setTo(to);



            message.setFrom(
                    System.getenv("MAIL_USERNAME")
            );



            message.setSubject(subject);


            message.setText(body);



            System.out.println(
                    "BEFORE mailSender.send()"
            );



            mailSender.send(message);



            System.out.println(
                    "AFTER mailSender.send()"
            );


            System.out.println(
                    "TEXT EMAIL SENT SUCCESSFULLY"
            );


            System.out.println("=================================");



        }
        catch(Exception e){


            System.out.println(
                    "TEXT EMAIL FAILED"
            );


            e.printStackTrace();



            throw new RuntimeException(
                    "Failed to send email",
                    e
            );

        }


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


        System.out.println(
                "Preparing Order Confirmation Email..."
        );


        // =================================================
        // PRODUCT ROWS
        // =================================================


        StringBuilder productRows =
                new StringBuilder();



        if(order.getOrderItems() != null){


            for(OrderItem item : order.getOrderItems()){


                productRows.append(

                """
                <tr>


                    <td style="
                        padding:14px;
                        border-bottom:1px solid #e5e7eb;
                        color:#333;
                        font-size:14px;
                    ">

                        %s

                    </td>



                    <td style="
                        padding:14px;
                        text-align:center;
                        border-bottom:1px solid #e5e7eb;
                        color:#333;
                        font-size:14px;
                    ">

                        %s

                    </td>



                    <td style="
                        padding:14px;
                        text-align:right;
                        border-bottom:1px solid #e5e7eb;
                        color:#333;
                        font-size:14px;
                    ">

                        ₹%s

                    </td>


                </tr>

                """.formatted(

                        item.getProductName(),

                        item.getQuantity(),

                        item.getSubTotal()

                ));


            }


        }




        // =================================================
        // DELIVERY ADDRESS
        // =================================================


        String address =
                "Address not available";



        if(order.getAddress()!=null){


            Address orderAddress =
                    order.getAddress();



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
        // HTML TEMPLATE
        // =================================================


        String html =

        """

        <!DOCTYPE html>

        <html>


        <body style="
            margin:0;
            padding:0;
            background:#f3f4f6;
            font-family:Arial;
        ">



        <div style="
            max-width:700px;
            margin:30px auto;
            background:white;
            border-radius:14px;
            overflow:hidden;
        ">



        <!-- HEADER -->


        <div style="
            background:#172554;
            padding:25px;
            text-align:center;
        ">


            <h1 style="
                color:#f59e0b;
            ">

                Manikanta Sales

            </h1>


            <p style="
                color:#dbeafe;
            ">

                Your trusted online shopping store

            </p>


        </div>





        <!-- CONTENT -->


        <div style="
            padding:30px;
        ">



        <div style="
            background:#dcfce7;
            padding:20px;
            border-radius:10px;
            text-align:center;
        ">


            <h2 style="
                color:#166534;
            ">

                ✓ Order Confirmed!

            </h2>


            <p>

                Your order has been placed successfully.

            </p>


        </div>





        <p>

            Hello

            <b>
                %s
            </b>


        </p>



        <p>

            Thank you for shopping with

            <b>
                Manikanta Sales
            </b>.


            Your order is being processed.

        </p>





        <!-- ORDER INFO -->


        <div style="
            background:#f8fafc;
            padding:18px;
            border-radius:10px;
        ">


            <p>

                <b>Order ID:</b>

                #%s

            </p>



            <p>

                <b>Status:</b>

                %s

            </p>



            <p>

                <b>Payment:</b>

                %s

            </p>


        </div>





        <h3>

            Order Items

        </h3>



        <table width="100%%"
               style="
               border-collapse:collapse;
               border:1px solid #ddd;
               ">


        <tr style="
            background:#172554;
            color:white;
        ">


            <th>
                Product
            </th>


            <th>
                Qty
            </th>


            <th>
                Amount
            </th>


        </tr>



        %s



        </table>





        <h2 style="
            text-align:right;
            color:#ea580c;
        ">


            Total : ₹%s


        </h2>





        <h3>

            Delivery Address

        </h3>



        <div style="
            background:#f8fafc;
            padding:15px;
            border-radius:8px;
        ">


            %s


        </div>





        <div style="
            margin-top:25px;
            background:#172554;
            padding:20px;
            color:white;
            text-align:center;
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

                order.getOrderStatus(),

                order.getPaymentStatus(),

                productRows.toString(),

                order.getAmount(),

                address

        );





        sendHtmlEmail(

                order.getUser().getEmail(),

                "Order Confirmed #" + order.getId(),

                html

        );



    }
    // =====================================================
    // ORDER CANCELLATION HTML EMAIL
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
        ">



        <!-- HEADER -->

        <div style="
            background:#172554;
            padding:25px;
            text-align:center;
        ">


            <h1 style="
                color:#f59e0b;
                margin:0;
            ">

                Manikanta Sales

            </h1>



            <p style="
                color:#dbeafe;
            ">

                Order Update

            </p>


        </div>





        <!-- CONTENT -->


        <div style="
            padding:30px;
        ">



        <!-- CANCEL BOX -->


        <div style="
            background:#fee2e2;
            border:1px solid #fca5a5;
            padding:20px;
            border-radius:10px;
            text-align:center;
        ">



            <h2 style="
                color:#b91c1c;
            ">

                ✕ Order Cancelled

            </h2>



            <p style="
                color:#991b1b;
            ">

                Your order has been cancelled successfully.

            </p>


        </div>





        <p>

            Hello

            <b>
                %s
            </b>


        </p>




        <p style="
            color:#555;
        ">


            Your order cancellation has been processed
            successfully.



        </p>






        <!-- ORDER DETAILS -->


        <div style="
            background:#f8fafc;
            padding:20px;
            border-radius:10px;
        ">



            <p>

                <b>
                    Order ID:
                </b>

                #%s


            </p>




            <p>

                <b>
                    Order Amount:
                </b>

                ₹%s


            </p>




            <p>

                <b>
                    Status:
                </b>


                <span style="
                    background:#fee2e2;
                    color:#b91c1c;
                    padding:5px 10px;
                    border-radius:20px;
                    font-weight:bold;
                ">


                    CANCELLED


                </span>


            </p>



        </div>






        <!-- INFO -->


        <div style="
            margin-top:25px;
            background:#fff7ed;
            padding:18px;
            border-radius:10px;
            color:#92400e;
        ">


            If you did not request this cancellation,
            please contact Manikanta Sales support.



        </div>






        <!-- FOOTER -->


        <div style="
            margin-top:25px;
            background:#172554;
            padding:20px;
            text-align:center;
            color:white;
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
        ">



        <!-- HEADER -->


        <div style="
            background:#172554;
            padding:25px;
            text-align:center;
        ">



            <h1 style="
                color:#f59e0b;
                margin:0;
            ">


                Manikanta Sales


            </h1>




            <p style="
                color:#dbeafe;
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



            <h2 style="
                color:#92400e;
            ">


                ↩ Return Request Submitted


            </h2>




            <p style="
                color:#92400e;
            ">


                We have received your return request.


            </p>



        </div>






        <p>

            Hello

            <b>
                %s
            </b>


        </p>






        <p style="
            color:#555;
            line-height:1.6;
        ">



            Your return request has been submitted
            successfully.


            Our team will review your request and
            update the return status.



        </p>







        <!-- ORDER DETAILS -->


        <div style="
            background:#f8fafc;
            padding:20px;
            border-radius:10px;
        ">



            <p>


                <b>
                    Order ID:
                </b>


                #%s



            </p>





            <p>


                <b>
                    Order Amount:
                </b>


                ₹%s



            </p>





            <p>


                <b>
                    Order Status:
                </b>


                %s



            </p>






            <p>


                <b>
                    Return Status:
                </b>




                <span style="
                    background:#fef3c7;
                    color:#92400e;
                    padding:5px 10px;
                    border-radius:20px;
                    font-weight:bold;
                ">



                    REQUESTED



                </span>



            </p>



        </div>








        <!-- FOOTER -->


        <div style="
            margin-top:25px;
            background:#172554;
            color:white;
            padding:20px;
            text-align:center;
            border-radius:10px;
        ">




            We will keep you updated.



            <br><br>



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


            System.out.println(
                    "TO : " + to
            );


            System.out.println(
                    "SUBJECT : " + subject
            );



            System.out.println(
                    "MAIL_USERNAME = "
                    + System.getenv("MAIL_USERNAME")
            );


            System.out.println(
                    "MAIL_PASSWORD = "
                    + System.getenv("MAIL_PASSWORD")
            );





            MimeMessage message =
                    mailSender.createMimeMessage();





            MimeMessageHelper helper =
                    new MimeMessageHelper(
                            message,
                            true,
                            "UTF-8"
                    );





            helper.setFrom(
                    System.getenv("MAIL_USERNAME")
            );



            helper.setTo(to);



            helper.setSubject(subject);



            helper.setText(
                    html,
                    true
            );





            System.out.println(
                    "BEFORE mailSender.send()"
            );



            mailSender.send(message);




            System.out.println(
                    "AFTER mailSender.send()"
            );



            System.out.println(
                    "HTML EMAIL SENT SUCCESSFULLY"
            );


            System.out.println(
                    "================================="
            );



        }
        catch(MessagingException e){


            System.out.println(
                    "HTML EMAIL FAILED"
            );


            e.printStackTrace();



            throw new RuntimeException(
                    "HTML email sending failed",
                    e
            );


        }
        catch(Exception e){


            System.out.println(
                    "EMAIL SENDING FAILED"
            );


            e.printStackTrace();



            throw new RuntimeException(
                    "Email sending failed",
                    e
            );


        }


    }



}