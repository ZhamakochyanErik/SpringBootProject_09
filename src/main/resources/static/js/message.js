$(document).ready(function () {


    $("#message-form").on("submit", function (event) {
        event.preventDefault();
        $("#message-spinner").show();
        $.ajax({
            type: "POST",
            url: "/user/message/add",
            contentType: false,
            processData: false,
            data: new FormData($("#message-form")[0]),
            success: function (messageDto) {
                var messageDiv = '<li class="right">';
                if(messageDto.from.profileImg == null){
                    messageDiv+='<img  src="/resources/images/profile.png" alt=""\n' +
                        'class="profile-photo-sm pull-right"/>\n';
                }else {
                    messageDiv+='<img src="/resources/users/' + messageDto.from.profileImg + '" alt=""\n' +
                        'class="profile-photo-sm pull-right"/>\n';
                }
                    messageDiv+='<div class="chat-item">\n' +
                    '<div class="chat-item-header">\n' +
                    '<h5>' + messageDto.from.name + ' '+ messageDto.from.surname + '</h5>\n' +
                    '<small class="text-muted">' + messageDto.sendDate + '</small>\n' +
                    '</div>\n';
                if(messageDto.message != null){
                    messageDiv+='<p>' + messageDto.message + '</p>\n';
                }
                if(messageDto.imgUrl != null){
                    messageDiv+='<p>\n' +
                        '<img style="max-width: 52%"\n' +
                        ' src="/resources/messages/' + messageDto.imgUrl + '">\n' +
                        '</p>\n';
                }
                messageDiv+='</div>\n' +
                    '</li>';
                $("#user-message-empty").remove();
                $("#message-blog").append(messageDiv);
                $("#message").val("");
                $("#image").val(null);
                $("#message-spinner").hide();
            },
            error: function () {
                $("#message").val("");
                $("#message-spinner").hide();
            }
        })
    });
});
