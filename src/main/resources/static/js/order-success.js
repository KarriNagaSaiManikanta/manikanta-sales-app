// =====================================
// ORDER SUCCESS JS
// MANIKANTA SALES
// =====================================


const token = localStorage.getItem("token");




// =====================================
// PAGE LOAD
// =====================================

document.addEventListener(
    "DOMContentLoaded",
    ()=>{


        if(!token){


            alert(
                "Please login first"
            );


            window.location.href =
            "login.html";


            return;

        }



        loadOrderDetails();



    }

);







// =====================================
// GET ORDER ID FROM URL
// =====================================


function getOrderId(){


    const params =
    new URLSearchParams(
        window.location.search
    );


    return params.get(
        "orderId"
    );

}









// =====================================
// LOAD ORDER DETAILS
// =====================================


function loadOrderDetails(){



    const orderId =
    getOrderId();





    if(!orderId){


        alert(
            "Order ID missing"
        );


        window.location.href =
        "my-orders.html";


        return;

    }








    fetch(

        "/api/orders/"+orderId,

        {

            method:"GET",

            headers:{


                "Authorization":

                "Bearer "+token


            }


        }

    )







    .then(response=>{


        if(!response.ok){


            return response.text()
            .then(message=>{


                throw new Error(
                    message ||
                    "Order not found"
                );


            });


        }



        return response.json();



    })










    .then(order=>{



        console.log(
            "Order Details:",
            order
        );





		document.getElementById(
		    "orderId"
		).innerText =
		"#" + order.id;








        document.getElementById(
            "amount"
        ).innerText =

        "₹"+order.amount;








        document.getElementById(
            "paymentStatus"
        ).innerText =

        order.paymentStatus;








        document.getElementById(
            "orderStatus"
        ).innerText =

        order.orderStatus;








        // PAYMENT METHOD

        const paymentMethod =
        document.getElementById(
            "paymentMethod"
        );


        if(paymentMethod){

            paymentMethod.innerText =
            order.paymentMethod;

        }








    })










    .catch(error=>{


        console.error(
            "Error:",
            error
        );


        alert(
            "Unable to load order details"
        );



    });



}