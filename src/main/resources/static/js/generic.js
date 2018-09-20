$(document).ready(function () {
    var page = 1;
    setInterval(loadMessages,2000);
    var notificationsCount;
    if($("#notificationCount").text() > 0){
       notificationsCount =  $("#notificationCount").text() ;
    }else {
        notificationsCount = 0;
    }

    $(document).on("click","#notification-li",function () {
        if($("#notOnlcick").attr("content")){
            var notifications = [];
            $.each($(".not-li"),function (i, li) {
                notifications[i] = $(li).attr("id");
            });
            if(notifications.length != 0){
                $.ajax({
                    type: "POST",
                    url: "/notification/status/update",
                    contentType: "application/json",
                    data: JSON.stringify(notifications),
                    success: function (integerDto) {
                        $("#notOnlcick").attr("false");
                        var size = parseInt(notificationsCount) - parseInt(integerDto.count);
                        if(size <= 0){
                            $("#notificationCount").text("");
                            $("#notificationTitle").text(notificationEmptyText);
                        }else {
                            $("#notificationCount").text(size);
                            $("#notificationTitle").text(messageTitleLeft + ' ' + size + ' ' + notificationTitleRight);
                            notificationsCount = size;
                        }
                    },
                    error:function () {
                        window.location = "/404";
                    }
                })
            }
        }
    });
    $(document).on("mouseover",".last_not",function () {
        $(".last_not").attr("class","not-li");
        if($("#notificationExists").attr("content")){
            $.ajax({
                type:"GET",
                url:"/notifications/page/" + page++,
                contentType:"application/json",
                success:function (notificationRespone) {
                    $("#notificationExists").attr("content",notificationRespone.notificationExists);
                    $.each(notificationRespone.notifications,function (i, notification) {
                        var liTag;
                        if(notification.type == "FRIEND_REQUEST"){
                            if(i+1 == notificationRespone.notifications.length && notificationRespone.notifications.length  == 10){
                                liTag = ' <li class="last_not not-li" id="' + notification.id + '">' +
                                    '<div style="margin: 20px">';
                            }else {
                                liTag = ' <li class="not-li" id="' + notification.id + '">' +
                                    '<div style="margin: 20px">';
                            }
                            if(notification.from.profileImg == null){
                                liTag += '<img class="rounded-circle" style="height: 46px;width: 49px;" src="/resources/images/profile.png">';
                            }else {
                                liTag += '<img class="rounded-circle" style="height: 46px;width: 49px" src="/resources/users/' + notification.from.profileImg + '">';
                            }
                            var nameText = notification.from.name + ' ' + notification.from.surname;
                            if ((notification.from.name.length + notification.from.surname.length + 1) > 11) {
                                nameText = nameText.substring(0, 11);
                            }

                            liTag += '<a style="margin: 10px;padding: 10px" href="/user/' + notification.from.id + '/profile">' + nameText + '</a>' +
                                '<div style="display: flex;justify-content: space-around">' +
                                '<p>' + friendRequesTitle + '</p>' +
                                '<a  class="add-friend" href="/friend/add/' + notification.from.id +  '"   id="' +  notification.from.id + '-add">' +
                                '<i class="fa fa-user-plus"></i>' +
                                '</a>' +
                                '<a class="delete-friend" href="/friend/delete/' + notification.from.id +  '" id="' +  notification.from.id + '-delete">' +
                                '<i class="fa fa-close"></i>' +
                                '</a>'+
                                '</div>' +
                                '</div>' +
                                '</li>' +
                                '<li>' +
                                '<hr/>' +
                                '</li>';
                        }else if(notification.type == "POST_COMMENT"){
                            if(i+1 == notificationRespone.notifications.length && notificationRespone.notifications.length  == 10){
                                liTag = ' <li class="last_not not-li" id="' + notification.id + '">' +
                                    '<div style="margin: 20px">';
                            }else {
                                liTag = ' <li class="not-li" id="' + notification.id + '">' +
                                    '<div style="margin: 20px">';
                            }
                            if(notification.from.profileImg == null){
                                liTag += '<img class="rounded-circle" style="height: 46px;width: 49px;" src="/resources/images/profile.png">';
                            }else {
                                liTag += '<img class="rounded-circle" style="height: 46px;width: 49px" src="/resources/users/' + notification.from.profileImg + '">';
                            }
                            var nameText = notification.from.name + ' ' + notification.from.surname;
                            if ((notification.from.name.length + notification.from.surname.length + 1) > 11) {
                                nameText = nameText.substring(0, 11);
                            }
                            liTag+=    '<a style="margin: 10px;padding: 10px" href="/user/' + notification.from.id + '/profile">' + nameText + '</a>' +
                                '<p>' +
                                '<i class="fa fa-comment"></i>' +
                                '<span>' + postCommentTitle + '</span>' +
                                '</p>' +
                                '<div style="margin: 10px">';
                            if(notification.post.imgUrl != null){
                                liTag+='<img class="rounded-circle" style="height: 38px;width: 39px" src="/resources/posts/' + notification.post.imgUrl + '">';
                            }
                            var title = "";

                            if (notification.post.title != null && notification.post.title.length > 11) {
                                title = notification.post.title.substring(0, 11) + '...';
                            } else if(notification.post.title != null){
                                title = notification.post.title;
                            }else if(notification.post.description != null && notification.post.description > 11){
                                title = notification.post.description.substring(0, 11) + '...';
                            }else if(notification.post.description != null){
                                title = notification.post.description;
                            }
                            liTag+= '<a href="/post/' + notification.post.id + '">' + title + '</a>' +
                                '</div>' +
                                '</div>' +
                                '</li>' +
                                '<li>' +
                                '<hr/>' +
                                '</li>';
                        }else if(notification.type == "POST_LIKE"){
                            if(i+1 == notificationRespone.notifications.length && notificationRespone.notifications.length  == 10){
                                liTag = ' <li class="last_not not-li" id="' + notification.id + '">' +
                                    '<div style="margin: 20px">';
                            }else {
                                liTag = ' <li class="not-li" id="' + notification.id + '">' +
                                    '<div style="margin: 20px">';
                            }
                            if(notification.from.profileImg == null){
                                liTag += '<img class="rounded-circle" style="height: 46px;width: 49px;" src="/resources/images/profile.png">';
                            }else {
                                liTag += '<img class="rounded-circle" style="height: 46px;width: 49px" src="/resources/users/' + notification.from.profileImg + '">';
                            }
                            var nameText = notification.from.name + ' ' + notification.from.surname;
                            if (nameText.length > 11) {
                                nameText = nameText.substring(0, 11);
                            }
                            liTag+= '<a style="margin: 10px;padding: 10px" href="/user/' + notification.from.id + '/profile">' + nameText + '</a>' +
                                '<p>' +
                                '<i class="ti-thumb-up"></i>' +
                                '<span>' + postLikeTitle + '</span>' +
                                '</p>' +
                                '<div style="margin: 10px">';
                            if(notification.post.imgUrl != null){
                                liTag+='<img class="rounded-circle" style="height: 38px;width: 39px" src="/resources/posts/' + notification.post.imgUrl + '">';
                            }
                            var title = "";

                            if (notification.post.title != null && notification.post.title.length > 11) {
                                title = notification.post.title.substring(0, 11) + '...';
                            } else if(notification.post.title != null){
                                title = notification.post.title;
                            }else if(notification.post.description != null && notification.post.description > 11){
                                title = notification.post.description.substring(0, 11) + '...';
                            }else if(notification.post.description != null){
                                title = notification.post.description;
                            }
                            liTag+= '<a href="/post/' + notification.post.id + '">' + title + '</a>' +
                                '</div>' +
                                '</div>' +
                                '</li>' +
                                '<li>' +
                                '<hr/>' +
                                '</li>';
                        }else {
                            if(i+1 == notificationRespone.notifications.length && notificationRespone.notifications.length  == 10){
                                liTag = ' <li class="last_not not-li" id="' + notification.id + '">' +
                                    '<div style="margin: 20px">';
                            }else {
                                liTag = ' <li class="not-li" id="' + notification.id + '">' +
                                    '<div style="margin: 20px">';
                            }
                            if(notification.from.profileImg == null){
                                liTag += '<img class="rounded-circle" style="height: 46px;width: 49px;" src="/resources/images/profile.png">';
                            }else {
                                liTag += '<img class="rounded-circle" style="height: 46px;width: 49px" src="/resources/users/' + notification.from.profileImg + '">';
                            }
                            var nameText = notification.from.name + ' ' + notification.from.surname;
                            if (nameText.length > 11) {
                                nameText = nameText.substring(0, 11);
                            }
                            liTag+= '<a style="margin: 10px;padding: 10px" href="/user/' + notification.from.id + '/profile">  ' + nameText + '</a>' +
                                '<p>' +
                                '<i class="ti-thumb-down"></i>' +
                                '<span>' + postDislikeTile + '</span>' +
                                '</p>' +
                                '<div style="margin: 10px">';
                            if(notification.post.imgUrl != null){
                                liTag+='<img class="rounded-circle" style="height: 38px;width: 39px" src="/resources/posts/' + notification.post.imgUrl + '">';
                            }
                            var title = "";

                            if (notification.post.title != null && notification.post.title.length > 11) {
                                title = notification.post.title.substring(0, 11) + '...';
                            } else if(notification.post.title != null){
                                title = notification.post.title;
                            }else if(notification.post.description != null && notification.post.description > 11){
                                title = notification.post.description.substring(0, 11) + '...';
                            }else if(notification.post.description != null){
                                title = notification.post.description;
                            }
                            liTag+= '<a href="/post/' + notification.post.id + '">' + title + '</a>' +
                                '</div>' +
                                '</div>' +
                                '</li>' +
                                '<li>' +
                                '<hr/>' +
                                '</li>';
                        }
                        $("#notification-ul").append(liTag);
                    })

                    var size = parseInt(notificationsCount) - parseInt(notificationRespone.count);
                    if(size <= 0){
                        $("#notificationCount").text("");
                        $("#notificationTitle").text(notificationEmptyText);
                    }else {
                        $("#notificationCount").text(size);
                        $("#notificationTitle").text(messageTitleLeft + ' ' + size + ' ' + notificationTitleRight);
                    }
                    notificationsCount = size;

                },
                error:function () {
                    window.location = "/404"
                }
            })
        }
    })
});
var messageTitleLeft = $("#messages-title-lefth").attr("content");
var messageTitleRight = $("#messages-title-right").attr("content");
var messageEmptyText = $("#messages-emty-text").attr("content");
function loadMessages() {
    $.ajax({
        type: "GET",
        url: "/message",
        contentType: "application/json",
        success: function (messages) {
            $("#message-ul").empty();
            if (messages.length == 0) {
                $("#messagesTile").text(messageEmptyText);
            } else {
                $.each(messages, function (i, message) {
                    $("#message-count").text(messages.length);
                    $("#messagesTile").text(messageTitleLeft + ' ' + messages.length + ' ' + messageTitleRight);
                    var liTag = '<li>' +
                        '<a href="/user/messages/to/' + message.from.id + '">' +
                        '<div class="pull-left">';
                    if (message.from.profileImg == null) {
                        liTag += '<img src="/resources/images/profile.png" class="rounded-circle" alt="' + message.from.name + '">'
                    } else {
                        liTag += '<img src="/resources/users/' + message.from.profileImg + '" class="rounded-circle" alt="' + message.from.name + '">'
                    }
                    liTag += '</div>' +
                        '<div class="mail-contnet">';
                    var nameText = message.from.name + ' ' + message.from.surname;
                    if ((message.from.name.length + message.from.surname.length + 1) > 11) {
                        liTag += '<h4>' + nameText.substring(0, 11);
                    } else {
                        liTag += '<h4>' + nameText;

                    }
                    liTag += '<small style="margin-right: -13px"><i class="fa fa-clock-o"></i>' + message.sendDate + '</small>' +
                        '</h4>';
                    if (message.message == null) {
                        liTag += '<span><img style="width: 30px;height: 30px;" src="/resources/messages/' + message.imgUrl + '"></span>';
                    } else if (message.message.length > 33) {
                        liTag += '<span>' + message.message.substring(0, 33) + '...</span>'
                    } else {
                        liTag += '<span>' + message.message + '</span>'
                    }
                    liTag += '</div>' +
                        '</a>' +
                        '</li>';
                    $("#message-ul").append(liTag);
                })
            }
        },
        error: function () {
            window.location = "/404";
        }
    });
}