let banners=[];

let index=0;



document.addEventListener(
"DOMContentLoaded",
()=>{


loadBanners();


});





function loadBanners(){


fetch("/api/banners")

.then(res=>res.json())

.then(data=>{


banners=data;


showBanner();


setInterval(
showBanner,
4000
);



});


}





function showBanner(){


let banner=banners[index];



document
.getElementById("title")
.innerHTML=banner.title;



document
.getElementById("description")
.innerHTML=banner.description;



document
.getElementById("offer")
.innerHTML="🔥 "+banner.offer;



document
.getElementById("bannerImage")
.src=banner.image;



index++;


if(index>=banners.length){

index=0;

}


}