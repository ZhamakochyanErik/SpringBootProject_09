$(document).ready(function () {

    $(document).on("click",".reply",function () {
        var id = $(this).attr("id");
        var clazz = $("#" + id + "-reply-form").attr("class");
        var postId = $("#" + id).children(".post-id").val();
        $(".open").empty();
        $(".open").attr("class","close");
        $("#reply-form_").remove();
        if(clazz == "close"){
            $("#" + id + "-reply-form").html(
                '<form id="reply-form_" class="publisher bt-1 border-fade bg-white reply-form">' +
                '<img class="avatar avatar-sm user-profile-image" src="' + $("#userImg").attr("content") + '" alt="...">' +
                '<input type="hidden" class="parent-id" value="' + id + '">' +
                '<input type="hidden" class="post-id_" value="' + postId + '">' +
                '<textarea  class="publisher-input reply-comment"  placeholder="' + $("#commentPlaceholder").attr("content") + '"></textarea>' +
                '<button type="submit" class="btn btn-info pull-right">' + $("#send").attr("content") + '</button>' +
                '</form>'
            );
            $("#" + id + "-reply-form").attr("class","open");
        }
    });

    $(document).on("submit",".reply-form",function (event) {
        event.preventDefault();
        var replyComment = $(this).children(".reply-comment");
        var value = replyComment.val();
        var postId = $(this).children(".post-id_").val();
        var parentId = $(this).children(".parent-id").val();
        if(value == null || value.length < 1){
            replyComment.css("border","1px solid red")
        }else {
            replyComment.css("border","none");
            $.ajax({
                type:"POST",
                url:"/post/comment",
                contentType: "application/json",
                data: JSON.stringify({
                    parentId: parentId,
                    postId: postId,
                    comment: value
                }),
                success: function (comment) {
                    var commentDiv = "";
                    commentDiv+='<div class="media px-0 mt-20">' +
                        '<a class="avatar" href="/user/' + comment.user.id + '/profile">';
                    if(comment.user.profileImg == null){
                        if(comment.user.id == $("#userId").attr("content")){
                            commentDiv+='<img class="user-profile-image" src="/resources/images/profile.png" alt="...">';
                        }else {
                            commentDiv+='<img src="/resources/images/profile.png" alt="...">';
                        }
                    }else {
                        if(comment.user.id == $("#userId").attr("content")){
                            commentDiv+='<img class="user-profile-image" src="/resources/users/' +  comment.user.profileImg + '" alt="...">';
                        }else {
                            commentDiv+='<img  src="/resources/users/' +  comment.user.profileImg + '" alt="...">';
                        }
                    }
                    commentDiv+='</a>' +
                        '<div class="media-body">' +
                        '<p>';
                    var nameText = comment.user.name + ' ' + comment.user.surname;

                    commentDiv+='<a href="/user/' + comment.user.id + '/profile"><strong>' + nameText + '</strong></a>' +
                        '<time class="float-right text-fade" datetime="2017-07-14 20:00">'+ comment.sendDate +
                        '</time>' +
                        '</p>' +
                        '<p>' + comment.comment + '</p>' +
                        '<a class="reply" id="' + comment.id + '" style="cursor: pointer;color: #69cce0;">' + $("#reply").attr("content") + '<i class="fa fa-reply"></i>' +
                        '<input type="hidden" class="post-id" value="' + postId + '"></a>' +
                        '<div id="' + comment.id + '-reply-form" class="close"></div><div id="' + comment.id +'-parent"></div></div></div>';
                    replyComment.val("");
                    $("#" +parentId + "-reply-form").before(commentDiv)
                    $("#" + postId + "-comment-count").text(
                        parseInt($("#" + postId + "-comment-count").text().split("-")[0]) + 1);
                },
                error: function () {
                    window.location = "/404";
                }
            })
        }
    });

    $(document).on("click",".load-comments",function () {
        var pageInput = $(this).children(".page_")
        var page = $(pageInput).val();
        if(page != "-1"){
            var child = $(this).children(".load-comment-icon");
            var postId = $(this).children(".post-id").val();
            child.attr("class","fa fa-spinner fa-pulse");
            $.ajax({
                type:"GET",
                url:"/post/" + postId + "/comments/page/" + page++,
                success: function (comments) {
                    if(comments.length == 0){
                        $(pageInput).val("-1");
                    }else {
                        $(pageInput).val(page);
                        $.each(comments,function (i,comment) {
                            var commentDiv = '<div class="media">' +
                                '<a class="avatar" href="/user/' + comment.commentDto.user.id + '/profile">';
                            if(comment.commentDto.user.profileImg == null){
                                if(comment.commentDto.user.id == $("#userId").attr("content")){
                                    commentDiv+='<img class="user-profile-image" src="/resources/images/profile.png" alt="...">';
                                }else {
                                    commentDiv+='<img src="/resources/images/profile.png" alt="...">';
                                }
                            }else {
                                if(comment.commentDto.user.id == $("#userId").attr("content")){
                                    commentDiv+='<img class="user-profile-image" src="/resources/users/' +  comment.commentDto.user.profileImg + '" alt="...">';
                                }else {
                                    commentDiv+='<img  src="/resources/users/' +  comment.commentDto.user.profileImg + '" alt="...">';
                                }
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
                                '<input type="hidden" class="post-id" value="' + postId +'"></a>' +
                                '<div id="' + comment.commentDto.id +'-parent">';
                            commentDiv+= setChildrens(comment.childrens,postId);
                            commentDiv+='<div id="' + comment.commentDto.id + '-reply-form" class="close"></div></div></div></div>';
                            $("#" + postId + "-comment-blog").append(commentDiv);
                        });
                    }
                    child.attr("class","fa fa-refresh");
                },
                error: function () {
                    window.location = "/404";
                }
            })
        }
    });

    $(document).on("submit",".comment-form",function (event) {
        event.preventDefault();
        var commentInput = $(this).children(".comment-text");
        var postId = $(this).children(".post-id").val();
        var addCommentIcon = $(this).children(".add-comment-icon");
        var commentText = commentInput.val();
        if(commentText == null || commentText.length == 0){
            commentInput.css("border","1px solid red")
        }else {
            addCommentIcon.show();
            commentInput.css("border","none");
            $.ajax({
                type: "POST",
                url: "/post/comment",
                contentType: "application/json",
                data: JSON.stringify({
                    postId: postId,
                    comment: commentText
                }),
                success: function () {
                    commentInput.val("");
                    page = 0;
                    isCommentsExists = true;
                    $.ajax({
                        type:"GET",
                        url:"/post/" + postId + "/comments/page/" + page++,
                        success: function (comments) {
                                $("#" + postId + "-comment-blog").empty();
                                $.each(comments,function (i,comment) {
                                    var commentDiv = '<div class="media">' +
                                        '<a class="avatar" href="/user/' + comment.commentDto.user.id + '/profile">';
                                    if(comment.commentDto.user.profileImg == null){
                                        if(comment.commentDto.user.id == $("#userId").attr("content")){
                                            commentDiv+='<img class="user-profile-image" src="/resources/images/profile.png" alt="...">';
                                        }else {
                                            commentDiv+='<img src="/resources/images/profile.png" alt="...">';
                                        }
                                    }else {
                                        if(comment.commentDto.user.id == $("#userId").attr("content")){
                                            commentDiv+='<img class="user-profile-image" src="/resources/users/' +  comment.commentDto.user.profileImg + '" alt="...">';
                                        }else {
                                            commentDiv+='<img  src="/resources/users/' +  comment.commentDto.user.profileImg + '" alt="...">';
                                        }
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
                                        '<input type="hidden" class="post-id" value="' + postId +'"></a>' +
                                        '<div id="' + comment.commentDto.id +'-parent">';
                                    commentDiv+= setChildrens(comment.childrens,postId);
                                    commentDiv+='<div id="' + comment.commentDto.id + '-reply-form" class="close"></div></div></div></div>';
                                    $("#" + postId + "-comment-blog").append(commentDiv);
                                });
                                $("#" + postId + "-comment-count").text(
                                    parseInt($("#" + postId + "-comment-count").text().split("-")[0]) + 1);
                            addCommentIcon.hide();
                        },
                        error: function () {
                            window.location = "/404";
                        }
                    })
                },
                error: function () {
                    window.location = "/404";
                }
            })
        }
    })
});

function setChildrens(childrenList,postId) {
    var commentDiv = "";
    $.each(childrenList,function (i, comment) {
        commentDiv+='<div class="media px-0 mt-20">' +
            '<a class="avatar" href="/user/' + comment.commentDto.user.id + '/profile">';
        if(comment.commentDto.user.profileImg == null){
            if(comment.commentDto.user.id == $("#userId").attr("content")){
                commentDiv+='<img class="user-profile-image" src="/resources/images/profile.png" alt="...">';
            }else {
                commentDiv+='<img src="/resources/images/profile.png" alt="...">';
            }
        }else {
            if(comment.commentDto.user.id == $("#userId").attr("content")){
                commentDiv+='<img class="user-profile-image" src="/resources/users/' +  comment.commentDto.user.profileImg + '" alt="...">';
            }else {
                commentDiv+='<img  src="/resources/users/' +  comment.commentDto.user.profileImg + '" alt="...">';
            }
        }

        commentDiv+='</a>' +
            '<div class="media-body">' +
            '<p>';
        var nameText = comment.commentDto.user.name + ' ' + comment.commentDto.user.surname;

        commentDiv+='<a href="/user/' + comment.commentDto.user.id + '/profile"><strong>' + nameText + '</strong></a>' +
            '<time class="float-right text-fade" datetime="2017-07-14 20:00">'+ comment.commentDto.sendDate +
            '</time>' +
            '</p>' +
            '<p>' + comment.commentDto.comment + '</p>' +
            '<a class="reply" id="' + comment.commentDto.id + '" style="cursor: pointer;color: #69cce0;">' + $("#reply").attr("content") + '<i class="fa fa-reply"></i>' +
            '<input type="hidden" class="post-id" value="' + postId +'"></a>' +
            '<div id="' + comment.commentDto.id +'-parent">';
        commentDiv+=setChildrens(comment.childrens,postId);
        commentDiv+='<div id="' + comment.commentDto.id + '-reply-form" class="close"></div></div></div></div>';
    });
    return commentDiv;
}