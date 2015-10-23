$(document).ready(function () {

    $("#grid").on('click', '.choose', function () {
        var difChosen = +"0";
        var itemId = $(this).attr("data-ad-id");
        var imgclass = $(this).children('img').attr('class');
        var img = $(this).children('img').attr('src');
        if (imgclass == 'chosen') {
            difChosen = +"-1";
            img = '../img/dop5.png';
            $(this).children('img').attr('src', img);
        } else {
            difChosen = +"1";
            img = '../img/dop5v2.png';
            $(this).children('img').attr('src', img);
        }
        $(this).children('img').toggleClass('chosen');
        var sumChosen = $('#chosenCount').text();
        if ($.isNumeric(sumChosen)) {
            sumChosen = Number(sumChosen);
            $('#chosenCount').html(sumChosen + difChosen);
        }
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
                        alert("При выполнении операции возникла ошибка, ответ сервера не удалось разобрать. Попробуйте обновить страницу и повторить операцию или обратитесь к системному администратору.");
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

    $("#grid").on('click', '.compareAdder', function () {
        var itemId = $(this).attr("data-ad-id");
        var imgclass = $(this).children('img').attr('class');
        if (imgclass != 'comparing') {
            var sumComp = $('#compareCount').text();
            if ($.isNumeric(sumComp)) {
                sumComp = Number(sumComp);
                $('#chosenCount').html(sumComp + Number(1));
                $(this).children('img').attr('class','comparing');
            }
        }
        addToCompare(itemId);
    });

    function addToCompare(adId) {
        $.ajax({
            url: "../Ad/addToComparison?adId=" + adId,
            dataType: "json",
            cache: false,
            //???
            success: function (json) {
                if (json['status'] == true) {
                    
                } else {
                    if (json['message'] != undefined) {
                        alert("error: " + json['message']);
                    } else {
                        alert("При выполнении операции возникла ошибка, ответ сервера не удалось разобрать. Попробуйте обновить страницу и повторить операцию или обратитесь к системному администратору.");
                    }
                }

            },
            error: function (json) {
                alert("Что-то пошло не так: " + json['message']);
            }
        });
    }

});