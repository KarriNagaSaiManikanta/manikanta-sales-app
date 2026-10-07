const token =
localStorage.getItem("token");



function saveAddress(){


let address={


name:
document.getElementById("name").value,


mobile:
document.getElementById("mobile").value,


houseNo:
document.getElementById("houseNo").value,


street:
document.getElementById("street").value,


city:
document.getElementById("city").value,


state:
document.getElementById("state").value,


pincode:
document.getElementById("pincode").value


};


fetch("/api/address/add",{

method:"POST",

headers:{
    "Authorization":"Bearer "+token,
    "Content-Type":"application/json"
},


body:
JSON.stringify(address)


})


.then(res=>{


if(!res.ok){

throw new Error();

}


return res.json();


})


.then(()=>{


alert(
"Address saved successfully"
);


window.location.href =
"checkout.html";


})


.catch(()=>{


alert(
"Address save failed"
);


});


}





function goBack(){

window.location.href =
"checkout.html";

}