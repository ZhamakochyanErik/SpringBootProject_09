$(document).ready(function () {
    var userId = $("#userId").attr("content");
    var cancelRequest = $("#cancelRequest").attr("content");
    var lang = $("#lang").attr("content");
    var addFriend = $("#addFriend").attr("content");

    $(document).on("click", "#add-friend", function (event) {
        event.preventDefault();
        $.ajax({
            type: "POST",
            url: "/friend-request/add",
            contentType: "application/json",
            data: JSON.stringify({
                "userId": userId
            }),
            success: function () {
                var size;
                if (lang == "en") {
                    size = 39;
                } else if (lang == "ru") {
                    size = 48;
                } else {
                    size = 71;
                }
                var requestDiv = '<th:block><div style="left: 25px;top: 7px;margin: -8px" class="btn-group">' +
                    '<button id="cancel-request" class="btn btn-social bg-danger mb-5">' +
                    '<i class="fa fa-close"></i> ' + cancelRequest +
                    '</button>' +
                    '</div></th:block>';
                $("#friend-request-blog").html(requestDiv);
            },
            error: function () {
                window.location = "/404";
            }
        })
    })


    $(document).on("click", "#cancel-request", function (event) {
        event.preventDefault();
        $.ajax({
            type: "POST",
            url: "/friend-request/delete",
            contentType: "application/json",
            data: JSON.stringify({
                "userId": userId
            }),
            success: function () {
                var requestDiv = "";
                if (lang == "en") {
                    requestDiv += '<th:block><div style="padding: 0px;left: 23px;position: relative;top: 9px;margin: -7px;" class="btn-group">\n' +
                        '<button id="add-friend" class="btn btn-social btn-twitter mb-5">\n' +
                        '<i class="fa fa-user-plus"></i> ' + addFriend +
                        '</button>\n' +
                        '</div></th:block>';
                } else {
                    requestDiv += '<th:block><div style="left: 17px;top: 5px;" class="btn-group">\n' +
                        '<button id="add-friend" class="btn btn-social btn-twitter mb-5">\n' +
                        '<i class="fa fa-user-plus"></i> ' + addFriend +
                        '</button>\n' +
                        '</div></th:block>'
                }
                $("#friend-request-blog").html(requestDiv);
            },
            error: function () {
                window.location = "/404";
            }
        })
    })

    $(document).on("click", "#accept-request", function (event) {
        event.preventDefault();

        $.ajax({
            type: "POST",
            url: "/friend-request/accepted",
            contentType: "application/json",
            data: JSON.stringify({
                "userId": userId
            }),
            success: function () {
                var requestDiv = '<th:blcok><div style="left: 25px;top: 7px;margin: -8px" class="btn-group">' +
                    '<button id="cancel-request" class="btn btn-social bg-danger mb-5">' +
                    '<i class="fa fa-close"></i> ' + cancelRequest +
                    '</button>' +
                    '</div></th:blcok>';
                $("#friend-request-blog").html(requestDiv);
            },
            error: function () {
                window.location = "/404";
            }
        })
    })
});