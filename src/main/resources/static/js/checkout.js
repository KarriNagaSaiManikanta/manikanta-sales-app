// =====================================
// CHECKOUT JS
// =====================================


const token = localStorage.getItem("token");

let cartData = [];

let selectedAddressId = null;

const shippingCharge = 50;




// =====================================
// PAGE LOAD
// =====================================

document.addEventListener(
    "DOMContentLoaded",
    ()=>{


        if(!token){

            alert("Please login first");

            window.location.href="login.html";

            return;

        }



        loadUser();

        loadAddresses();

        loadCart();


    }

);






// =====================================
// LOAD USER
// =====================================

function loadUser(){


    fetch(
        "/api/users/profile",
        {

            headers:{

                "Authorization":
                "Bearer "+token

            }

        }
    )


    .then(res=>res.json())


    .then(user=>{


        const element =
        document.getElementById("userName");


        if(element){

            element.innerText =
            user.firstName;

        }


    })

    .catch(err=>{

        console.log(err);

    });


}







// =====================================
// LOAD ADDRESS
// =====================================
function loadAddresses(){

    fetch("/api/address/my",{
        headers:{
            "Authorization":"Bearer " + token
        }
    })
    .then(response=>{
        if(!response.ok){
            throw new Error("Unable to load addresses");
        }
        return response.json();
    })
    .then(addresses=>{

        if(!Array.isArray(addresses)){
            return;
        }

        let html = "";

        addresses.forEach(address=>{

            html += `
            <div class="address-item">

                <input
                    type="radio"
                    name="address"
                    value="${address.id}"
                    onclick="selectAddress(${address.id})">

                <b>${address.name}</b>

                <p>${address.houseNo}, ${address.street}</p>

                <p>${address.city}, ${address.state} - ${address.pincode}</p>

                <p>Mobile : ${address.mobile}</p>

            </div>
            `;

        });

        document.getElementById("addressList").innerHTML = html;

    })
    .catch(error=>{
        console.error(error);
    });

}


// =====================================
// SELECT ADDRESS
// =====================================

function selectAddress(id){


    selectedAddressId=id;


}








// =====================================
// LOAD CART
// =====================================

function loadCart(){

    fetch(
        "/api/cart",
        {
            headers:{
                "Authorization":"Bearer "+token
            }
        }
    )

    .then(res=>res.json())

    .then(cart=>{


        console.log("Cart:",cart);


        cartData = cart;


        let html="";
        let total=0;



        cart.forEach(item=>{


            let subtotal =
            item.price * item.quantity;


            total += subtotal;



            html += `

            <div class="checkout-product">


                <img src="${item.productImage}">


                <div class="checkout-product-info">


                    <h3>
                        ${item.productName}
                    </h3>


                    <p>
                        Brand : ${item.brand}
                    </p>


                    <p>
                        Category : ${item.category}
                    </p>


                    <p>
                        Price : ₹${item.price}
                    </p>


                    <p>
                        Quantity : ${item.quantity}
                    </p>


                    <b>
                        Sub Total : ₹${subtotal}
                    </b>


                </div>


            </div>

            `;


        });



        document.getElementById(
            "cartItems"
        ).innerHTML = html;



        document.getElementById(
            "productTotal"
        ).innerHTML =
        "₹"+total;



        document.getElementById(
            "totalAmount"
        ).innerHTML =
        "₹"+(total+shippingCharge);



    })

    .catch(error=>{

        console.log(
            "Cart Error",
            error
        );

    });

}





// =====================================
// PLACE ORDER
// =====================================

function placeOrder() {

    if (!selectedAddressId) {
        alert("Please select delivery address");
        return;
    }

    const paymentElement =
        document.querySelector('input[name="payment"]:checked');

    if (!paymentElement) {
        alert("Please select payment method");
        return;
    }

    const placeBtn = document.getElementById("placeOrderBtn");

    placeBtn.disabled = true;
    placeBtn.innerHTML = "Placing Order...";

    const request = {
        addressId: selectedAddressId,
        paymentMethod: paymentElement.value,
        shippingCharge: shippingCharge
    };

    console.log("Order Request :", request);

    fetch("/api/orders/place", {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": "Bearer " + token
        },
        body: JSON.stringify(request)
    })

    .then(async response => {

        const text = await response.text();

        console.log("RAW RESPONSE :", text);

        if (!response.ok) {
            throw new Error(text);
        }

        return JSON.parse(text);
    })

    .then(order => {

        console.log("ORDER OBJECT :", order);

        if (!order.id) {
            alert("Order ID Missing");
            return;
        }

        alert("✅ Order Placed Successfully");

        setTimeout(() => {

            window.location.href =
                "order-success.html?orderId=" + order.id;

        }, 1000);

    })

    .catch(error => {

        console.error(error);

        alert(error.message);

    })

    .finally(() => {

        placeBtn.disabled = false;
        placeBtn.innerHTML = "Place Order";

    });

}

// =====================================
// ADD ADDRESS
// =====================================

function addNewAddress(){

    window.location.href =
    "add-address.html";

}

