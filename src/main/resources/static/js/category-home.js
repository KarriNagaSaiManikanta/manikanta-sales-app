const CATEGORY_API = "/api/categories";


document.addEventListener(
"DOMContentLoaded",
()=>{

    loadHomeCategories();

});



function loadHomeCategories(){


fetch(CATEGORY_API)

.then(res=>res.json())

.then(data=>{


    let categories = data.content 
        ? data.content 
        : data;


    displayHomeCategories(categories);


})

.catch(err=>{

console.log(err);

});


}




function displayHomeCategories(categories){


const container =
document.getElementById(
"homeCategoryContainer"
);



let html="";


categories.forEach(category=>{


html += `


<div class="category-card"
onclick="openCategory(${category.id})">


<img src="${category.image 
? category.image 
: '../images/no-image.png'}">


<h3>
${category.name}
</h3>


</div>


`;


});


container.innerHTML=html;


}




function openCategory(categoryId){


window.location.href =
"products.html?categoryId="+categoryId;


}