$(document).ready(function () {
    var error = $("#registerError").attr("content");
    var email = $("#email").attr("placeholder");
    var password = $("#password").attr("placeholder");

    $("#login-form").on("submit", function (event) {
        var emailValue = $("#email").val();
        if (emailValue == null || emailValue < 11) {
            $("#email").css("background-image", "linear-gradient(red,red), linear-gradient(red,red)");
            $("#email").attr("class","form-control text-white place-red");
            $("#email").attr("placeholder",error);
            event.preventDefault();
        } else {
            $("#email").css("background-image", "linear-gradient(green , green), linear-gradient(green, green)");
            $("#email").attr("class","form-control text-white place-white");
            $("#email").attr("placeholder",email);
        }
        var passwordValue = $("#password").val();
        if (passwordValue == null || passwordValue < 4) {
            $("#password").css("background-image", "linear-gradient(red,red), linear-gradient(red,red)");
            $("#password").attr("placeholder",error);
            $("#password").attr("class","form-control text-white place-red");
            event.preventDefault();
        } else {
            $("#password").css("background-image", "linear-gradient(green , green), linear-gradient(green, green)");
            $("#password").attr("placeholder",password);
            $("#password").attr("class","form-control text-white place-white");
        }
    })
});