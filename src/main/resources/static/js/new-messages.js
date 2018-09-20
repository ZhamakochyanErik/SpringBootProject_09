$(document).ready(function () {
    setInterval(loadNewMessages,200);

})


function loadNewMessages() {
    $.ajax({
        type:"GET",
        url:"/user/messages/to/" + $("#user-id").val() + "/load/new",
        contentType:"application/json",
        success:function (messageDtoList) {
            $.each(messageDtoList,function (i, messageDto) {
                var messageDiv = '<li class="right new-messages" id="' + messageDto.id +'">';
                if(messageDto.from.profileImg == null){
                    messageDiv+='<img  src="/resources/images/profile.png" alt=""\n' +
                        'class="profile-photo-sm pull-left"/>\n';
                }else {
                    messageDiv+='<img src="/resources/users/' + messageDto.from.profileImg + '" alt=""\n' +
                        'class="profile-photo-sm pull-left"/>\n';
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
            })
        },
        error: function () {
        }
    })
}