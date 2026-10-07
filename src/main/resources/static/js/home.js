document.addEventListener("DOMContentLoaded",()=>{


const productContainer =
document.getElementById(
"product-container"
);



if(!productContainer){

console.log(
"Product container not found"
);

return;

}




loadProducts();





async function loadProducts(){


try{


const response =
await fetch(
"http://localhost:8080/api/products?page=0&size=8"
);



const data =
await response.json();



displayProducts(
data.content
);



}

catch(error){


console.log(
"Product loading error:",
error
);



productContainer.innerHTML =

`

<h3>
Products not available
</h3>

`;


}



}








function displayProducts(products){



productContainer.innerHTML="";




products.forEach(product=>{



productContainer.innerHTML +=

`

<div class="product-card">



<img src="${product.image}"
alt="${product.name}">



<h3>

${product.name}

</h3>



<p class="price">

₹${product.price}

</p>




<div class="actions">


<button onclick="addCart(${product.id})">

Add Cart

</button>



<button onclick="addWishlist(${product.id})">

❤️

</button>



</div>



</div>

`;



});



}







});







// ======================
// CART
// ======================


function addCart(id){


let cart =

JSON.parse(
localStorage.getItem("cart")
)

|| [];



cart.push(id);



localStorage.setItem(
"cart",
JSON.stringify(cart)
);



alert(
"Added to Cart"
);



}





// ======================
// WISHLIST
// ======================


function addWishlist(id){


let wishlist =

JSON.parse(
localStorage.getItem("wishlist")
)

|| [];



wishlist.push(id);



localStorage.setItem(
"wishlist",
JSON.stringify(wishlist)
);



alert(
"Added to Wishlist"
);



}