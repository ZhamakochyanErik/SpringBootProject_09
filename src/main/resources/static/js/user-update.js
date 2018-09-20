$(document).ready(function () {
    loadCities();
})

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
                if($("#cityId_").attr("content") == city.id){
                    var option = "<option value='" + city.id + "' selected>" + cityName + "</option>";
                }else {
                    var option = "<option value='" + city.id + "'>" + cityName + "</option>";
                }
                $("#city-select").append(option);
            })
        },
        error: function () {
            window.location = "/404";
        }
    })
}