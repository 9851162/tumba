$(document).ready(function () {

$(function() {
          $( ".isDatepicker" ).datepicker();
        });

    var i = 0,
            $metro = $('#grid'),
            $items = $metro.find('.item'),
            $defItems = $items.clone(true);

    $('#grid').find('.item').each(function () {
        $(this).attr('item-position', i);
        if (i < $items.length) {
            $(this).attr('item-next', i + 1);
        }
        i++;
    });

    $('#grid').on('click', '.smal', function () {
        $metro.html($items);
        $items.removeClass('medium');
        $items.removeClass('big');
        $items.addClass('smal');
        var metroWidth = $metro.width(),
                itemWidth = $items.eq(0).width(),
                metroInLine = Math.floor(metroWidth / itemWidth),
                prevCnt, i;

        var itemPos = parseInt($(this).attr('item-position'));

        $('.thumbs img').on('click', function () {
            var gallery = $(this).closest('.toblockimg');
            gallery.find('.largeImage').attr('src', $(this).attr('src').replace('thumb', 'large'));
        });


        if (itemPos % metroInLine == 0) {

            $(this).addClass('medium');
            $(this).removeClass('smal');
            $(this).removeClass('big');
        } else {

            prevCnt = (itemPos > metroInLine) ? itemPos % metroInLine : itemPos;

            for (i = 1; i <= prevCnt; i++) {
                $(this).after($(this).prev());
            }

            $(this).addClass('medium');
            $(this).removeClass('smal');
        }
        addWatch($(this).attr('data-ad-id'));
    });
    $('#grid').on('click', '.button_expand', function () {
        $('.medium').addClass("big");
        $('.big').removeClass("medium");
    });
    $('#grid').on('click', '.button_rollUp', function () {
        $('.big').addClass("smal");
        $('.smal').removeClass("big");
    });


    function addWatch(adId){
        $.ajax({
            url: "../Ad/watch?adId=" + adId,
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
                }

            },
            error: function (json) {
                alert("Что-то пошло не так: " + json['message']);
            }
        });
    }

})