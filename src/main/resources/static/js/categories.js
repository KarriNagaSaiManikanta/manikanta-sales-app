const CATEGORY_API = "/api/categories";



document.addEventListener(
"DOMContentLoaded",
()=>{

    loadCategories();

});




// ===============================
// LOAD CATEGORIES
// ===============================

function loadCategories(){


fetch(CATEGORY_API)

.then(res=>res.json())

.then(data=>{


    console.log("CATEGORY DATA",data);


    displayCategories(data.content);


})

.catch(err=>{

console.error(
"Category loading failed",
err
);


});


}







// ===============================
// DISPLAY CATEGORY BUTTONS
// ===============================

function displayCategories(categories){


const container =

document.getElementById(
"categoryContainer"
);



let html = `


<button onclick="loadAllProducts()">

All

</button>


`;




categories.forEach(category=>{


html += `


<button

onclick="loadProductsByCategory(${category.id})">

${category.name}

</button>


`;


});



container.innerHTML = html;


}