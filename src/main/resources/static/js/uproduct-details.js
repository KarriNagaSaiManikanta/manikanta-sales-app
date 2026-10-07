// =====================================
// API
// =====================================

const API = "/api/products";

const SERVER_URL = "http://localhost:8080";


// =====================================
// PRODUCT ID
// =====================================

const productId =
new URLSearchParams(window.location.search).get("id");


// =====================================
// QUANTITY
// =====================================

let quantity = 1;

let availableStock = 0;


// =====================================
// PAGE LOAD
// =====================================

document.addEventListener("DOMContentLoaded",()=>{


    if(!productId){

        alert("Invalid Product");

        window.location.href="/products.html";

        return;

    }


    loadProduct();

    loadRelatedProducts();


});



// =====================================
// LOAD PRODUCT
// =====================================

function loadProduct(){


const token = localStorage.getItem("token");


fetch(`${API}/${productId}`,{


    headers:{


        "Authorization":
        "Bearer "+token


    }


})


.then(response=>{


    console.log("Product Status :",response.status);


    if(!response.ok){

        throw new Error(
            "Product not found"
        );

    }


    return response.json();


})


.then(product=>{


    console.log("Product Data :",product);


    showProduct(product);


})


.catch(error=>{


    console.error(
        "Load Product Error:",
        error
    );


    alert(
        "Unable to load product."
    );


});


}






// =====================================
// SHOW PRODUCT
// =====================================

function showProduct(product){



let image = product.image;


/*
 image database:
 /uploads/products/file.jpg
*/

if(image){

    if(!image.startsWith("http")){

        image = SERVER_URL + image;

    }

}

else{

    image="/images/no-image.png";

}





// IMAGE

document.getElementById("productImage").src=image;


document.getElementById("thumb1").src=image;

document.getElementById("thumb2").src=image;

document.getElementById("thumb3").src=image;






// NAME

document.getElementById("productName").textContent =

product.name || "";





// PRICE

document.getElementById("productPrice").textContent =

product.price || 0;






// BRAND

document.getElementById("productBrand").textContent =

product.brand || "Generic";







// CATEGORY

document.getElementById("productCategory").textContent =

product.category ?

product.category.name :

"No Category";






// DESCRIPTION

document.getElementById("productDescription").textContent =

product.description ||

"No Description Available";








// SPECIFICATIONS


document.getElementById("specBrand").textContent =

product.brand || "-";





document.getElementById("specCategory").textContent =

product.category ?

product.category.name :

"-";





document.getElementById("specPrice").textContent =

"₹ "+product.price;








// ==============================
// STOCK
// ==============================


availableStock = product.quantity || 0;


document.getElementById("productQuantity").innerText =

availableStock;





const stock =

document.getElementById("stockStatus");



if(stock){


    if(availableStock > 0){


        stock.innerText =

        "In Stock ("+

        availableStock+

        " available)";


        stock.style.color="green";


    }

    else{


        stock.innerText=

        "Out Of Stock";


        stock.style.color="red";


    }


}





document.getElementById("specStock").textContent =


availableStock > 0 ?

"In Stock" :

"Out Of Stock";



}








// =====================================
// THUMB CLICK
// =====================================


document
.querySelectorAll(".thumb-images img")
.forEach(img=>{


img.addEventListener("click",function(){


document.getElementById("productImage").src=this.src;


});


});









// =====================================
// QUANTITY PLUS
// =====================================


function increaseQty(){



if(quantity < availableStock){


quantity++;


document.getElementById("qty").value=

quantity;


}

else{


alert(

"Only "+availableStock+" items available"

);


}


}







// =====================================
// QUANTITY MINUS
// =====================================


function decreaseQty(){


if(quantity > 1){


quantity--;


document.getElementById("qty").value=

quantity;


}


}








// =====================================
// ADD CART
// =====================================

// =====================================
// ADD CART
// =====================================

function addToCart(){


const token = localStorage.getItem("token");


if(!token){

    alert("Please login first");

    window.location.href="/login.html";

    return;

}



if(availableStock <= 0){


    alert(
    "Product Out Of Stock"
    );

    return;

}





fetch(

`/api/cart/add?productId=${productId}&quantity=${quantity}`,

{

method:"POST",

headers:{


"Authorization":
"Bearer "+token


}


}

)



.then(response=>{


if(!response.ok){

throw new Error(
"Unable to add cart"
);

}


return response.text();


})



.then(()=>{


alert(
"Product added to Cart 🛒"
);



window.location.href="/cart.html";



})



.catch(error=>{


console.log(error);


alert(
"Cart add failed"
);



});


}



// =====================================
// BUY NOW
// =====================================


function buyNow(){


alert(
"Proceeding to Checkout..."
);


}







// =====================================
// WISHLIST
// =====================================

// =====================================
// ADD WISHLIST
// =====================================

function addWishlist(){


const token = localStorage.getItem("token");


if(!token){

    alert("Please login first");

    window.location.href="/login.html";

    return;

}



fetch(

`/api/wishlist/add?productId=${productId}`,

{

method:"POST",

headers:{

"Authorization":
"Bearer "+token

}

}

)



.then(response=>response.text().then(msg=>{


    if(response.ok){


        alert(
        "Added to Wishlist ❤️"
        );


    }

    else{


        if(msg.includes("Already in wishlist")){


            alert(
            "Product already in Wishlist ❤️"
            );


        }

        else{


            alert(
            "Wishlist failed"
            );


        }


    }


}))



.catch(error=>{


console.log(error);


alert(
"Server error"
);


});


}




// =====================================
// RELATED PRODUCTS CATEGORY WISE
// =====================================

function loadRelatedProducts(){


fetch(
`${API}/${productId}`
)


.then(response=>response.json())


.then(currentProduct=>{


const categoryId =

currentProduct.category.id;



return fetch(

`${API}/category/${categoryId}?page=0&size=5`

);



})


.then(response=>response.json())


.then(data=>{


let html="";


data.content.forEach(product=>{


// current product skip

if(product.id == productId)

return;



let image = product.image;



if(image && !image.startsWith("http")){


image =
"http://localhost:8080"+image;


}

else if(!image){


image="/images/no-image.png";


}



html += `


<div class="related-card">


<img src="${image}"

onerror="this.src='/images/no-image.png'">



<h4>

${product.name}

</h4>



<p>

₹ ${product.price}

</p>



<button

onclick="window.location.href='/uproduct-details.html?id=${product.id}'">


View Details


</button>



</div>


`;



});




document.getElementById(
"relatedProducts"
).innerHTML = html;



})

.catch(error=>{


console.log(
"Related Category Error:",
error
);


});


}