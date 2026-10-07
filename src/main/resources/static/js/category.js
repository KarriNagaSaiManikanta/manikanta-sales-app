// =======================================
// CATEGORY LIST JS
// Manikanta Sales Admin
// =======================================


// =======================================
// VARIABLES
// =======================================


let currentPage = 0;

let pageSize = 10;

let deleteId = null;





// =======================================
// ELEMENTS
// =======================================


const tableBody =
    document.getElementById("categoryTable");


const searchInput =
    document.getElementById("searchInput");


const prevBtn =
    document.getElementById("prevBtn");


const nextBtn =
    document.getElementById("nextBtn");


const pageInfo =
    document.getElementById("pageInfo");


const loader =
    document.getElementById("loader");


const toast =
    document.getElementById("toast");







// =======================================
// LOADER
// =======================================


function showLoader(){

    if(loader)
        loader.style.display="flex";

}



function hideLoader(){

    if(loader)
        loader.style.display="none";

}









// =======================================
// TOAST
// =======================================


function showToast(message){


    if(!toast)
        return;



    toast.innerHTML = message;


    toast.classList.add("show");



    setTimeout(()=>{


        toast.classList.remove("show");


    },3000);



}









// =======================================
// LOAD CATEGORY
// =======================================


async function loadCategories(){


    showLoader();



    try{


        const response =

        await apiRequest(

            CATEGORY_API
            +
            `?page=${currentPage}&size=${pageSize}`

        );




        const data =

        await response.json();





        renderTable(

            data.content

        );





        updatePagination(data);



    }


    catch(error){


        showToast(
            "Unable to load categories"
        );


    }


    finally{


        hideLoader();


    }


}











// =======================================
// TABLE RENDER
// =======================================


function renderTable(categories){


    tableBody.innerHTML="";





    if(!categories ||
       categories.length===0){



        tableBody.innerHTML = `

        <tr>

        <td colspan="6">

        No Categories Found

        </td>

        </tr>

        `;


        return;


    }






    categories.forEach(category=>{


        const image =

        category.image

        ?

        `

        <img

        class="category-img"

        src="http://localhost:8080${category.image}"

        onerror="this.src='../images/no-image.png'"

        >

        `

        :

        "No Image";







        tableBody.innerHTML += `


        <tr>


        <td>

        ${category.id}

        </td>





        <td>

        ${image}

        </td>





        <td>

        ${category.name}

        </td>





        <td>

        ${category.description || "-"}

        </td>





        <td>


        ${
            category.active

            ?

            `<span class="status active">

            Active

            </span>`

            :

            `<span class="status inactive">

            InActive

            </span>`
        }



        </td>





        <td>


        <button

        class="btn-edit"

        onclick="editCategory(${category.id})">

        Edit

        </button>






        <button

        class="btn-danger"

        onclick="openDelete(${category.id})">

        Delete

        </button>



        </td>




        </tr>



        `;



    });



}











// =======================================
// PAGINATION
// =======================================


function updatePagination(data){



    pageInfo.innerHTML =

    `Page ${data.number + 1}

     of ${data.totalPages}`;



    prevBtn.disabled =
        data.first;



    nextBtn.disabled =
        data.last;



}









// =======================================
// NEXT
// =======================================


nextBtn.onclick = ()=>{


    currentPage++;


    loadCategories();


};







// =======================================
// PREVIOUS
// =======================================


prevBtn.onclick = ()=>{


    currentPage--;


    loadCategories();


};









// =======================================
// SEARCH
// =======================================


let searchTimer;



searchInput.addEventListener(

"keyup",

()=>{


clearTimeout(searchTimer);



searchTimer = setTimeout(async()=>{



    const keyword =

    searchInput.value.trim();





    if(keyword===""){


        currentPage=0;


        loadCategories();


        return;


    }






    try{


        const response =

        await apiRequest(

            CATEGORY_API+

            "/search?keyword="+

            keyword

        );




        const data =

        await response.json();




        renderTable(data);



    }

    catch(error){


        showToast(
            "Search failed"
        );


    }



},500);



});











// =======================================
// EDIT
// =======================================


function editCategory(id){



    window.location.href =

    "edit-category.html?id="+id;



}











// =======================================
// DELETE MODAL
// =======================================


function openDelete(id){



    deleteId=id;



    document

    .getElementById("deleteModal")

    .style.display="flex";



}






function closeDelete(){



    document

    .getElementById("deleteModal")

    .style.display="none";



    deleteId=null;



}









// =======================================
// DELETE
// =======================================


// =======================================
// DELETE CATEGORY
// =======================================

async function confirmDelete() {

    if (!deleteId) {
        return;
    }

    console.log("DELETE ID :", deleteId);
    console.log("TOKEN :", getToken());
    console.log("URL :", CATEGORY_API + "/" + deleteId);

    try {

        const response = await fetch(CATEGORY_API + "/" + deleteId, {
            method: "DELETE",
            headers: {
                "Authorization": "Bearer " + getToken()
            }
        });

        console.log("STATUS :", response.status);

        const text = await response.text();
        console.log("RESPONSE :", text);

        if (response.ok) {

            showToast("Category Deleted Successfully");

            closeDelete();

            loadCategories();

        } else {

            showToast("Delete Failed : " + response.status);

        }

    } catch (error) {

        console.error(error);

        showToast(error.message);

    }

}

// =======================================
// START
// =======================================


loadCategories();