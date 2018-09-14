$(document).ready(function () {
    loadCities();
    $("#register-form").on("submit",function (event) {
        var inputTags = $(".reg-input");
        var size = 0;

        $.each(inputTags,function (i, input) {
            var name = $(input).attr("name");
            var value = $(input).val();
            if(isValidData(name,value,event,input)){
                size++;
            }
        })
        if(size == 7){
            if($("#password").val() != $("#rePassword").val()){
                event.preventDefault();
                $("#password").attr("style","background-image: linear-gradient(red ,red), linear-gradient(red, red)");
                $("#password").attr("class","form-control text-white place-red reg-input")
                $("#rePassword").attr("style","background-image: linear-gradient(red ,red), linear-gradient(red, red)");
                $("#rePassword").attr("class","form-control text-white place-red reg-input")
            }
        }
    })
});

function isValidData(name, value, event,input) {
    switch (name){
        case "name":
            if(value == null || value.length < 2 || value.length > 255){
                $(input).attr("style","background-image: linear-gradient(red ,red), linear-gradient(red, red)");
                $(input).attr("class","form-control text-white place-red reg-input")
                event.preventDefault();
                return false;
            }else {
                $(input).attr("style","background-image: linear-gradient(#69cce0 , #69cce0), linear-gradient(#d9d9d9, #d9d9d9)");
                $(input).attr("class","form-control text-white place-white reg-input");
                return true;
            }
            break;
        case "surname":
            if(value == null || value.length < 2 || value.length > 255){
                $(input).attr("style","background-image: linear-gradient(red ,red), linear-gradient(red, red)");
                $(input).attr("class","form-control text-white place-red reg-input")
                event.preventDefault();
                return false;
            }else {
                $(input).attr("style","background-image: linear-gradient(#69cce0 , #69cce0), linear-gradient(#d9d9d9, #d9d9d9)");
                $(input).attr("class","form-control text-white place-white reg-input");
                return true;
            }
            break;
        case "email":
            if(value == null || value.length < 11 || value.length > 255){
                $(input).attr("style","background-image: linear-gradient(red ,red), linear-gradient(red, red)");
                $(input).attr("class","form-control text-white place-red reg-input")
                event.preventDefault();
                return false;
            }else {
                $(input).attr("style","background-image: linear-gradient(#69cce0 , #69cce0), linear-gradient(#d9d9d9, #d9d9d9)");
                $(input).attr("class","form-control text-white place-white reg-input");
                return true;
            }
            break;
        case "password":
            if(value == null || value.length < 4 || value.length > 255){
                $(input).attr("style","background-image: linear-gradient(red ,red), linear-gradient(red, red)");
                $(input).attr("class","form-control text-white place-red reg-input")
                event.preventDefault();
                return false;
            }else {
                $(input).attr("style","background-image: linear-gradient(#69cce0 , #69cce0), linear-gradient(#d9d9d9, #d9d9d9)");
                $(input).attr("class","form-control text-white place-white reg-input");
                return true;
            }
            break;
        case "rePassword":
            if(value == null || value.length < 4 || value.length > 255){
                $(input).attr("style","background-image: linear-gradient(red ,red), linear-gradient(red, red)");
                $(input).attr("class","form-control text-white place-red reg-input")
                event.preventDefault();
                return false;
            }else {
                $(input).attr("style","background-image: linear-gradient(#69cce0 , #69cce0), linear-gradient(#d9d9d9, #d9d9d9)");
                $(input).attr("class","form-control text-white place-white reg-input");
                return true;
            }
            break;
        case "birthDate":
            if(value == null || value.length != 10 ){
                $(input).attr("style","background-image: linear-gradient(red ,red), linear-gradient(red, red)");
                $(input).attr("class","form-control text-white place-red reg-input")
                event.preventDefault();
                return false;
            }else {
                $(input).attr("style","background-image: linear-gradient(#69cce0 , #69cce0), linear-gradient(#d9d9d9, #d9d9d9)");
                $(input).attr("class","form-control text-white place-white reg-input");
                return true;
            }
            break;
        case "cityId":
            if(value == null || value<= 0){
                $(input).attr("style","background-image: linear-gradient(red ,red), linear-gradient(red, red)");
                $(input).attr("class","form-control reg-input")
                event.preventDefault();
                return false;
            }else {
                $(input).attr("style","background-image: linear-gradient(#69cce0 , #69cce0), linear-gradient(#d9d9d9, #d9d9d9)");
                $(input).attr("class","form-control reg-input");
                return true;
            }
            break;
    }
}

function loadCities() {
    $.ajax({
        type:"GET",
        url: "/city",
        contentType: "application/json",
        success: function (cityArray) {
            $.each(cityArray,function (i, city) {
                var cityName;
                var lang= $("#lang").attr("content");
                if(lang == "en"){
                    cityName = city.nameEn;
                }else if(lang == "ru"){
                    cityName = city.nameRu;
                }else {
                    cityName = city.nameArm;
                }
                var option = "<option value='" + city.id + "'>" + cityName + "</option>";
                $("#city-select").append(option);
            })
        },
        error: function () {
            window.location = "/404";
        }
    })
}