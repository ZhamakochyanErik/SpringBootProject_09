$(document).ready(function () {
    setInterval(loadMessages,2000);

    $(document).on("click","#notification-li",function () {
        if($("#notificationCount").text() > 0){
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
                    success: function () {
                        $("#notificationCount").text("");
                        $("#notificationTitle").text(notificationEmptyText);
                        $("#notification-li").attr("id","notification-li_");
                    },
                    error:function () {
                        window.location = "/404";
                    }
                })
            }
        }
    });
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