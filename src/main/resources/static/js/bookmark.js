$(document).ready(function () {
    $("#add-bookmark").on("click",function () {
        $.ajax({
            type: "POST",
            url:"/user/bookmark",
            contentType:"application/json",
            data: JSON.stringify({
                "userId":$("#userId").attr("content")
            }),
            success:function (integerDto) {
                var status = integerDto.count;
                if(status == 200){
                   $("#add-bookmark").css("color","yellow");
                }else {
                    $("#add-bookmark").attr("style","cursor: pointer;position: relative;left: 20px;font-size: 20px")
                }
            },
            error: function () {

            }
        })
    })
})