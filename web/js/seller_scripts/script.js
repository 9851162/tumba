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
        //alert("qwe")
        $metro.html($items);
        $items.removeClass('medium');
		$items.removeClass('big');
		$items.addClass('smal');
        var metroWidth = $metro.width(),
            itemWidth = $items.eq(0).width(),
            metroInLine = Math.floor(metroWidth / itemWidth),
            prevCnt, i;

        var itemPos = parseInt($(this).attr('item-position'));


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
         
         $('#grid').on('click', '.prev4change', function(){
    var gallery = $(this).closest('.toblockimg');
    var srcimage = $(this).attr('src');
    gallery.find('.largeImage').attr('src',srcimage/*$(this).attr('src').replace('thumb','large')*/); 
    
}); 



})