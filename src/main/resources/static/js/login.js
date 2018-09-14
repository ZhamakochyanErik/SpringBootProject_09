$(document).ready(function () {

    $("#login-form").on("submit", function (event) {
        var emailValue = $("#email").val();
        if (emailValue == null || emailValue.length < 11) {
            $("#email").css("background-image", "linear-gradient(red,red), linear-gradient(red,red)");
            $("#email").attr("class","form-control text-white place-red");
            event.preventDefault();
        } else {
            $("#email").css("background-image", "linear-gradient(green , green), linear-gradient(green, green)");
            $("#email").attr("class","form-control text-white place-white");
        }
        var passwordValue = $("#password").val();
        if (passwordValue == null || passwordValue.length < 4) {
            $("#password").css("background-image", "linear-gradient(red,red), linear-gradient(red,red)");
            $("#password").attr("class","form-control text-white place-red");
            event.preventDefault();
        } else {
            $("#password").css("background-image", "linear-gradient(green , green), linear-gradient(green, green)");
            $("#password").attr("class","form-control text-white place-white");
        }
    })
});