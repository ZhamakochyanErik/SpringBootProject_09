$(document).ready(function () {
    var makeProfileImage = $("#makeProfielImage").attr("content");
    var makeCoverImage = $("#makeCoverImage").attr("content");
    var imagesLength= $("#imagesLength").attr("content");

    $(".img-click").on("click", function (event) {
        event.preventDefault();
        var src = $(this).children().attr("src");
        var id = $(this).attr("id");
        var index = id.split("-")[0];
        var imageId = $(this).children().attr("id").split("-")[0];
        var imgDiv = '<div id="img-slider" ' +
            'class="ekko-lightbox modal fade in show" ' +
            'tabindex="-1" ' +
            'role="dialog" ' +
            'style="display: block; padding-left: 17px;" ><div class="modal-dialog" ' +
            'role="document" ' +
            'style="display: block; width: auto; max-width: 513px;" > <div class="modal-content" style="height: 430px">' +
            ' <div class="modal-header" > ';
        if($("#isCurrentUser").attr("content") == "true"){
            imgDiv+='<a id="image-delete"' +
                ' href="/user/images/' + imageId + '/delete">' +
                ' <i class="fa fa-trash"> </i></a>';
        }
        imgDiv+= ' <button id="img-slider-close" type="button"' +
            ' class="close">×<span></button> ';

        if($("#isCurrentUser").attr("content") == "true"){
            imgDiv+='<div style="margin-top: -1px;display: inline-grid;margin-left: 35px;">' +
                '<a id="make-profile-image" style="cursor: pointer">' +makeCoverImage + '  <i class="fa fa-user-circle-o"> </i></a>' +
                '<a id="make-cover-image" style="cursor: pointer">' +  makeProfileImage + '  <i class="fa fa-image"> </i></a>' +'</div>';
        }
        imgDiv+='</div><div class="modal-body"><div class="ekko-lightbox-container" style="height: 99px;"><div class="ekko-lightbox-item fade">' +
            '</div><div class="ekko-lightbox-item fade in show">' +
            '<img id="img-' + index+'" src="' + src + '" class="img-fluid current-img" ' +
            ' style="width:100%;height: 330px"></div><div class="ekko-lightbox-nav-overlay"><a style="margin-top: 150px;cursor: pointer" id="prev" ><span>❮</span> ' +
            '</a><a id="next" style="margin-top: 150px;cursor: pointer"><span>❯</span > ' +
            '</a></div></div></div><div class="modal-footer hide"\n' +
            'style = "display: none;" ><\div></div></div></div>';
        $("#body_").append(imgDiv)
    });

    $(document).on("click","#next",function () {
        var id = $(".current-img").attr("id");
        var index = parseInt(id.split("-")[1]) + 1;
        if(index > imagesLength){
            index = 1;
        }
        var imageId = $("#" + index + '-index').children().attr("id").split("-")[0];
        var src = $("#" + index + '-index').children().attr("src");
        $("#image-delete").attr("href",'href="/user/images/' + imageId + '/delete"');
        $(".current-img").attr("id","img-" + index);
        $(".current-img").attr("src",src);
    })

    $(document).on("click","#prev",function () {
        var id = $(".current-img").attr("id");
        var index = parseInt(id.split("-")[1]) - 1;
        if(index < 1){
            index = imagesLength;
        }
        var imageId = $("#" + index + '-index').children().attr("id").split("-")[0];
        var src = $("#" + index + '-index').children().attr("src");
        $("#image-delete").attr("href",'href="/user/images/' + imageId + '/delete"');
        $(".current-img").attr("id","img-" + index);
        $(".current-img").attr("src",src);
    });

    $(document).on("click","#img-slider-close",function () {
        $("#img-slider").remove();
    });

    $(document).on("click","#make-profile-image",function () {
        $(this).append("   <i id='img-slid_' class='fa fa-spinner fa-pulse'></i>")
        var id = $(".current-img").attr("id");
        var index = id.split("-")[1];
        var imgId = $("#" + index + "-index").children().attr("id").split("-")[0];
        $.ajax({
            type: "POST",
            url: "/user/images/change/profile/image",
            contentType:"application/json",
            data: JSON.stringify({
                "imgId": imgId
            }),
            success: function (stringDto) {
                var src = "/resources/users/" + stringDto.str;
                $("#user-profile-img").attr("src",src);
                $("#user-profile-img").parent().attr("href",src);
                $("#profile-img_").attr("src",src);
                $("#img-slid_").remove();
            },
            error: function () {
                $("#img-slid_").remove();
            }
        })
    })

    $(document).on("click","#make-cover-image",function () {
        $(this).append("   <i id='img-slid__' class='fa fa-spinner fa-pulse'></i>")
        var id = $(".current-img").attr("id");
        var index = id.split("-")[1];
        var imgId = $("#" + index + "-index").children().attr("id").split("-")[0];
        $.ajax({
            type: "POST",
            url: "/user/images/change/cover/image",
            contentType:"application/json",
            data: JSON.stringify({
                "imgId": imgId
            }),
            success: function (stringDto) {
                var src = "/resources/users/" + stringDto.str;
                $("#user-cover-img").attr("style","background-image:url(" + src + ")");
                $("#cover-image__").attr("src",src);
                $("#img-slid__").remove();
            },
            error: function () {
                $("#img-slid__").remove();
            }
        })
    })
})