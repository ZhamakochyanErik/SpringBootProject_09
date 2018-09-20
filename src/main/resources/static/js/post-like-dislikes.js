$(document).ready(function () {

    $(document).on("click",".like-post",function () {
        var postId = $(this).attr("id").split("-")[0];
        $.ajax({
            type: "POST",
            url:"/post/like/add",
            contentType:"application/json",
            data:JSON.stringify({
                "postId" : postId
            }),
            success: function (integerDto) {
                var statusCode = integerDto.count;
                if(statusCode == 201){
                    $("#" + postId + '-dislike-img').attr("src","/resources/icons/dislike.png");
                    $("#" + postId + "-dislikeCount").text(' ' + (parseInt($("#" + postId + "-dislikeCount").text().split("-")[0]) - 1));
                    $("#" + postId + '-like-img').attr("src","/resources/icons/like_active.png");
                    $("#" + postId + "-likeCount").text(' ' + (parseInt($("#" + postId + "-likeCount").text().split("-")[0]) + 1));
                }else if(statusCode == 200){
                    $("#" + postId + '-like-img').attr("src","/resources/icons/like_active.png");
                    $("#" + postId + "-likeCount").text(' ' + (parseInt($("#" + postId + "-likeCount").text().split("-")[0]) + 1));
                }else {
                    $("#" + postId + '-like-img').attr("src","/resources/icons/like.png");
                    $("#" + postId + "-likeCount").text(' ' + (parseInt($("#" + postId + "-likeCount").text()) - 1));
                }
            },
            error: function () {
                window.location = "/error";
            }
        })
    });

    $(document).on("click",".dislike-post",function () {
        var postId = $(this).attr("id").split("-")[0];
        $.ajax({
            type: "POST",
            url:"/post/dislike/add",
            contentType:"application/json",
            data:JSON.stringify({
                "postId" : postId
            }),
            success: function (integerDto) {
                var statusCode = integerDto.count;
                if(statusCode == 201){
                    $("#" + postId + '-like-img').attr("src","/resources/icons/like.png");
                    $("#" + postId + "-likeCount").text(' ' + (parseInt($("#" + postId+"-likeCount").text().split("-")[0]) - 1));

                    $("#" + postId + '-dislike-img').attr("src","/resources/icons/dislike_active.png");
                    $("#" + postId + "-dislikeCount").text(' ' + (parseInt($("#" + postId + "-dislikeCount").text().split("-")[0]) +1));
                }else if(statusCode == 200){
                    $("#" + postId + '-dislike-img').attr("src","/resources/icons/dislike_active.png");
                    $("#" + postId + "-dislikeCount").text(' ' + (parseInt($("#" + postId + "-dislikeCount").text().split("-")[0]) +1));
                }else {
                    $("#" + postId + '-dislike-img').attr("src","/resources/icons/dislike.png");
                    $("#" + postId + "-dislikeCount").text(' ' + (parseInt($("#" + postId + "-dislikeCount").text().split("-")[0]) - 1));
                }
            },
            error: function () {
                window.location = "/error";
            }
        })
    })
});