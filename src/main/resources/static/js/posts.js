$(document).ready(function () {
    var page_ = 0;
    loadPosts(page_);

    $("#load-posts").on("click", function () {
        if (isExistsPosts_) {
            $("#loader").show();
            page_++;
            loadPosts(page_)
        }
    })

    $(document).on("click",".delete-post",function () {
        var id = $(this).attr("id").split("-")[0];
        var clazz = $("#"+ id +"-delete-div").attr("class");
        if(clazz == "open"){
            $("#"+ id +"-delete-div").attr("class","close")
            $("#" + id + "-yes").remove();
            $("#" + id + "-no").remove();
        }else {
            $("#"+ id +"-delete-div").attr("class","open")
            var appendDiv = "<a class='yes' style='cursor: pointer;margin-right: 20px' id='" + id + "-yes'><i class='fa fa-check'></i></a>" +
                "<a class='no' style='cursor: pointer' id='" + id + "-no'><i class='fa fa-close'></i></a>";
            $("#"+ id +"-delete-div").append(appendDiv);
        }
    });

    $(document).on("click",".no",function () {
        var id = $(this).attr("id").split("-")[0];
        $("#" + id + "-delete-div").empty();
        $("#" + id + "-delete-div").attr("class","close");
    });

    $(document).on("click",".yes",function () {
        var id = $(this).attr("id").split("-")[0];
        window.location = "/post/" + id + "/delete";
    })
});
var isExistsPosts_ = true;
var first = true;

function loadPosts(page_) {
    $.ajax({
        type: "GET",
        url: "/posts/page/" + page_,
        contentType: "application/json",
        success: function (posts) {
            if (posts.length == 0) {
                isExistsPosts_ = false;
                if(first){
                    $("#post-title-text").text($("#postsEmptyText").attr("content"));
                    $("#loader").remove();
                    $("#load-posts").remove();
                    first = false;
                }
            } else {
                if(first){
                    first=false;
                }
                $.each(posts, function (i, postData) {
                    var postDiv = '<div class="col-lg-6" style="margin-left: 300px">' +
                        '<div class="box">' +
                        '<div class="media bb-1 border-fade">';
                    if(postData.post.user.profileImg == null){
                        postDiv+='<a href="/user/' + postData.post.user.id + '/profile"><img class="avatar avatar-lg user-profile-iamge" src="/resources/images/profile.png" alt="..."></a>';
                    }else {
                        postDiv+='<a href="/user/' + postData.post.user.id + '/profile"><img class="avatar avatar-lg user-profile-iamge" src="/resources/users/' + postData.post.user.profileImg + '" alt="..."></a>';
                    }
                    postDiv+= '<div class="media-body">' +
                        '<p>' +
                        '<a href="/user/' + postData.post.user.id + '/profile"><strong>' + postData.post.user.name + ' ' + postData.post.user.surname + '</strong></a>';
                    var createdDate = formatDate(new Date(postData.post.createdDate));
                    postDiv += '<time class="float-right text-lighter" datetime="2017" >' + createdDate + '</time>' +
                        '</p></div>' +
                        '</div>' +
                        '<div class="box-body bb-1 border-fade">';
                    if (postData.post.imgUrl != null) {
                        postDiv += '<a href="/post/' + postData.post.id + '">' +
                            '<img style="width: 80%;height: 40%" src="/resources/posts/' + postData.post.imgUrl + '">' +
                            '</a>';
                    }
                    if (postData.post.title != null) {
                        postDiv += '<a href="/post/' + postData.post.id + '">';
                        var postTitle = postData.post.title;
                        if (postTitle.length > 40) {
                            postTitle = postTitle.substring(0, 40) + "...";
                        }
                        postDiv += '<h2 style="color: #69cce0">' + postTitle + '</h2></a>';
                    }
                    if (postData.post.description != null) {
                        postDiv += '<a href="/post/' + postData.post.id + '">';
                        var postDescription = postData.post.description;
                        if (postDescription.length > 150) {
                            postDescription = postDescription.substring(0, 150) + "...";
                        }
                        postDiv += '<p class="lead">' + postDescription + '</p></a>';
                    }
                    if($("#userId").attr("content") == postData.post.user.id){
                        postDiv+='<div><button type="button" id="' + postData.post.id + '-delete" class="btn btn-default btn-sm bg-blue-active delete-post"><i class="fa fa-close"></i>  ' + $("#deteteText").attr("content") + '</button></div>';
                        var lang = $("#lang").attr("content");
                        if(lang == "arm"){
                            postDiv+='<div style="margin-left: 11px" id="' + postData.post.id +'-delete-div" class="close"></div>'
                        }else if(lang == "en"){
                            postDiv+='<div style="margin-left: 16px" id="' + postData.post.id +'-delete-div" class="close"></div>'
                        }else {
                            postDiv+='<div style="margin-left: 19px" id="' + postData.post.id +'-delete-div" class="close"></div>'
                        }

                    }
                    postDiv += '<div class="gap-items-4 mt-10">' +
                        '<a class="text-lighter hover-light like-post" id="' + postData.post.id + '-like" style="cursor: pointer">';
                    if (postData.liked) {
                        postDiv += '<img id="' + postData.post.id + '-like-img"  src="/resources/icons/like_active.png">';
                    } else {
                        postDiv += '<img id="' + postData.post.id + '-like-img"  src="/resources/icons/like.png">';
                    }
                    postDiv += '<span id="' + postData.post.id +'-likeCount"> ' + postData.likesCount + '</span></a>' +
                        '<a class="text-lighter hover-light dislike-post" id="' + postData.post.id + '-dislike" style="cursor: pointer">';
                    if (postData.disliked) {
                        postDiv += '<img id="' + postData.post.id + '-dislike-img"  src="/resources/icons/dislike_active.png">';
                    } else {
                        postDiv += '<img id="' + postData.post.id + '-dislike-img"  src="/resources/icons/dislike.png">';
                    }
                    postDiv += '<span id="' + postData.post.id + '-dislikeCount"> ' + postData.dislikesCount + '</span></a>' +
                        '<a class="text-lighter hover-light" style="cursor: pointer">' +
                        '<i class="fa fa-comment mr-1"></i>' +
                        '<span id="' + postData.post.id + '-comment-count"> ' + postData.commentsCount + '</span>' +
                        '</a>' +
                        '</div>' +
                        '</div>' +
                        '<div class="media-list media-list-divided bg-lighter" id="' + postData.post.id + '-comment-blog">' +
                        '</div>' +
                        '<input type="hidden" id="postId" value="' + postData.post.id + '">' +
                        '<a class="load-comments" style="cursor: pointer;color: #69cce0;margin-left: 20px">\n' +
                        '<input type="hidden" class="post-id" value="' + postData.post.id + '">' +
                        '<input type="hidden" class="page_" value="1">' +
                        '<span>' + $("#loadMore").attr("content") + '  </span>' +
                        '<i class="fa fa-refresh load-comment-icon"></i>' +
                        '</a>' +
                        '<form class="publisher bt-1 border-fade bg-white comment-form" >' +
                        '<i style="display: none" class="fa fa-spinner fa-pulse add-comment-icon"></i>' +
                        '<input type="hidden" class="post-id" value="' + postData.post.id + '">' +
                        '<img class="avatar avatar-sm" src="' + $("#userImg").attr("content") + '" alt="...">' +
                        '<textarea class="publisher-input comment-text"  placeholder="' + $("#commentPlaceholder").attr("content") + '"></textarea>' +
                        '<button type="submit" class="btn btn-info pull-right">' + $("#send").attr("content") + '</button>' +
                        '</form>' +
                        '</div>' +
                        '</div>';
                    $("#post-blog").append(postDiv);
                    loadComments_(postData.post.id);
                })
            }
            $("#loader").hide();
        },
        error: function () {
            window.location = "/404";
        }
    })
}

function loadComments_(postId) {
    $.ajax({
        type:"GET",
        url:"/post/" + postId + "/comments/page/" + 0,
        success: function (comments) {
            $.each(comments,function (i,comment) {
                var commentDiv = '<div class="media">' +
                    '<a class="avatar" href="/user/' + comment.commentDto.user.id + '/profile">';
                if(comment.commentDto.user.profileImg == null){
                    commentDiv+='<img class="user-profile-iamge" src="/resources/images/profile.png" alt="...">';
                }else {
                    commentDiv+='<img class="user-profile-iamge" src="/resources/users/' +  comment.commentDto.user.profileImg + '" alt="...">';
                }

                commentDiv+='</a>' +
                    '<div class="media-body">' +
                    '<p>';
                var nameText = comment.commentDto.user.name + ' ' + comment.commentDto.user.surname;

                commentDiv+='<a href="/user/' + comment.commentDto.user.id + '/profile"><strong>' + nameText + '</strong></a>' +
                    '<time class="float-right text-fade" datetime="2017-07-14 20:00">' + comment.commentDto.sendDate + '</time>' +
                    '</p>' +
                    '<p>' + comment.commentDto.comment + '</p>' +
                    '<a class="reply" id="' + comment.commentDto.id + '" style="cursor: pointer;color: #69cce0;">' + $("#reply").attr("content") + '<i class="fa fa-reply"></i>' +
                    '<input type="hidden" class="post-id" value="' + postId + '"></a>' +
                    '<div id="' + comment.commentDto.id +'-parent">';
                commentDiv+= setChildrens(comment.childrens,postId);
                commentDiv+='<div id="' + comment.commentDto.id + '-reply-form" class="close"></div></div></div></div>';
                $("#" + postId + "-comment-blog").append(commentDiv);
            })
        },
        error: function () {
            window.location = "/404";
        }
    })
}

function formatDate(createdDate) {
    var date = createdDate.getDate();
    if (date < 10) {
        date = "0" + date;
    }
    var month = createdDate.getMonth();
    if (month < 10) {
        month = "0" + month;
    }
    var hour = createdDate.getHours();
    var minute = createdDate.getMinutes();
    var second = createdDate.getSeconds();
    return date + "." + month + " " + hour + ":" + minute + ":" + second;
}