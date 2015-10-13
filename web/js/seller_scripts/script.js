$(document).ready(function(){



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

    $('#grid').on('click','.smal',function () {
        $metro.html($items);
        $items.removeClass('medium');
		$items.removeClass('big');
		$items.addClass('smal');
        var metroWidth = $metro.width(),
            itemWidth = $items.eq(0).width(),
            metroInLine = Math.floor(metroWidth / itemWidth),
            prevCnt, i;

        var itemPos = parseInt($(this).attr('item-position'));

		$('.thumbs img').on('click', function(){
			var gallery = $(this).closest('.toblockimg');
			gallery.find('.largeImage').attr('src',$(this).attr('src').replace('thumb','large'));
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

    });
	 $('#grid').on('click','.button_expand',function () {
			 $('.medium').addClass("big");
			 $('.big').removeClass("medium");
	 });
	 $('#grid').on('click','.button_rollUp',function () {
			 $('.big').addClass("smal");
			 $('.smal').removeClass("big");
	 });



})