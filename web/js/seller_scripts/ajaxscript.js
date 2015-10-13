$(document).ready(function () {

    $("#grid").on('click','.choose', function () {
        var itemId = $(this).attr("data-ad-id");
        var imgclass = $(this).children('img').attr('class');
        var img = $(this).children('img').attr('src');
        if (imgclass == 'chosen') {
            img = '../img/dop5.png';
            $(this).children('img').attr('src', img);
        } else {
            img = '../img/dop5v2.png';
            $(this).children('img').attr('src', img);
        }
        $(this).children('img').toggleClass('chosen');
        setChosenUnchosen(itemId);
    });

    function setChosenUnchosen(adId) {
        $.ajax({
            url: "../Ad/setChosenUnchosen?adId=" + adId,
            dataType: "json",
            cache: false,
            //???
            success: function (json) {
                if (json['status'] == true) {
                    //changebleElem.html(name);
                } else {
                    if (json['message'] != undefined) {
                        alert("error: " + json['message']);
                    } else {
                        alert("При обновлении параметра возникла ошибка, ответ сервера не удалось разобрать. Попробуйте обновить страницу и повторить операцию или обратитесь к системному администратору.");
                    }

                    /*$('#modalerror').html(json)
                     $('#overlay').fadeIn(400, //пoкaзывaем oверлэй
                     function () { // пoсле oкoнчaния пoкaзывaния oверлэя
                     $('#modalerror') // берем стрoку с селектoрoм и делaем из нее jquery oбъект
                     .css('display', 'block')
                     .animate({opacity: 1, top: '0%'}, 200); // плaвнo пoкaзывaем
                     });*/
                }

            },
            error: function (json) {
                alert("Что-то пошло не так: " + json['message']);
            }
        });
    }

});