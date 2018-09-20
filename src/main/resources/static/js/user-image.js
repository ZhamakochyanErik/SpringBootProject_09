$(document).ready(function () {

    $("#profileImage").on("change",function () {
        var form = $("#profileImage").parent();
        $(form).append("<i id='slider_' class='fa fa-spinner fa-pulse'></i>");
        $.ajax({
            type: "POST",
            url: "/user/images/profile/upload",
            contentType:false,
            processData:false,
            data: new FormData(form[0]),
            success: function (stringDto) {
                var src = "/resources/users/" + stringDto.str;
                $("#user-profile-img").attr("src",src);
                $("#user-profile-img").parent().attr("href",src);
                $("#profile-img_").attr("src",src);
                $(".user-profile-image").attr("src",src);
                $("#userImg").attr("content",src);
                $("#slider_").remove();
            },
            error: function () {
                $("#slider_").remove();
            }
        })
    });

    $("#coverImage").on("change",function () {
        var form = $("#coverImage").parent();
        $(form).append("<i id='slider__' class='fa fa-spinner fa-pulse'></i>");
        $.ajax({
            type: "POST",
            url: "/user/images/cover/upload",
            contentType:false,
            processData:false,
            data: new FormData(form[0]),
            success: function (stringDto) {
                var src = "/resources/users/" + stringDto.str;
                $("#user-cover-img").attr("style","background-image:url(" + src + ")");
                $("#cover-image__").attr("src",src);
                $("#slider__").remove();
            },
            error: function () {
                $("#slider__").remove();
            }
        })
    })
})