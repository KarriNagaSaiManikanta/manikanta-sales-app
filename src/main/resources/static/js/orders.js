// =====================================
// MY ORDERS JS
// =====================================


const token =
localStorage.getItem("token");



document.addEventListener(
"DOMContentLoaded",
()=>{


if(!token){

window.location.href="login.html";

return;

}



loadOrders();



});





// =====================================
// LOAD ORDERS
// =====================================


function loadOrders(){



	fetch("/api/orders/my", {
	    headers: {
	        "Authorization": "Bearer " + token
	    }


})


.then(res=>{


if(!res.ok){

throw new Error();

}


return res.json();


})


.then(orders=>{


console.log(
"ORDERS",
orders
);


displayOrders(orders);


})


.catch(error=>{


console.log(error);


document.getElementById(
"ordersContainer"
).innerHTML=`

<div class="empty">

Unable to load orders

</div>

`;


});



}








// =====================================
// DISPLAY ORDERS
// =====================================


function displayOrders(orders){



const container =
document.getElementById(
"ordersContainer"
);




if(!orders || orders.length===0){


container.innerHTML=`

<div class="empty">


<h2>
No Orders Found
</h2>


<p>
Start shopping now
</p>


</div>


`;

return;


}






let html="";




orders.forEach(order=>{


let item =

order.items && order.items.length > 0

?

order.items[0]

:

null;




html+=`

<div class="order-card">


<div class="order-top">


<div>


<div class="order-id">

Order #${order.id}

</div>



<div class="date">

${order.createdAt || ""}

</div>


</div>



<div class="status ${order.orderStatus}">

${order.orderStatus}

</div>


</div>





<div class="order-product">


<img 

src="${
item && item.productImage
?
item.productImage
:
'images/no-image.png'
}"

onerror="this.src='images/no-image.png'"

>




<div>


<div class="product-name">

${

item

?

item.productName

:

"Product"

}

</div>




<p>

Quantity :

${

item

?

item.quantity

:

1

}

</p>




<p class="price">

₹${

item

?

item.subTotal

:

order.amount

}

</p>



</div>



</div>





<div class="order-bottom">


<div class="amount">

Total ₹${order.amount}

</div>



<a class="view-btn"

href="order-details.html?orderId=${order.id}">

View Details

</a>



</div>



</div>


`;



});




container.innerHTML=html;


}