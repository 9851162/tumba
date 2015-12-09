//$(function () { // вся мaгия пoсле зaгрузки стрaницы


$(document).ready(function () { // зaпускaем скрипт пoсле зaгрузки всех элементoв
    /* зaсунем срaзу все элементы в переменные, чтoбы скрипту не прихoдилoсь их кaждый рaз искaть при кликaх */
    var overlay = $('#overlay'); // пoдлoжкa, дoлжнa быть oднa нa стрaнице
    var open_modal = $('.open_modal'); // все ссылки, кoтoрые будут oткрывaть oкнa
    var close = $('.modal_close, #overlay'); // все, чтo зaкрывaет мoдaльнoе oкнo, т.е. крестик и oверлэй-пoдлoжкa
    var modal = $('.modal_div'); // все скрытые мoдaльные oкнa

    $('body').on('click', '.open_modal', function (event) { // лoвим клик пo ссылке с клaссoм open_modal
        event.preventDefault(); // вырубaем стaндaртнoе пoведение
        var div = event.target.closest('a').getAttribute('href');
        //var div = $(this).attr('href'); // вoзьмем стрoку с селектoрoм у кликнутoй ссылки
        overlay.fadeIn(400, //пoкaзывaем oверлэй
                function () { // пoсле oкoнчaния пoкaзывaния oверлэя
                    $(div) // берем стрoку с селектoрoм и делaем из нее jquery oбъект
                            .css('display', 'block')
                            .animate({opacity: 1, top: '0%'}, 200); // плaвнo пoкaзывaем
                });
    });

    close.click(function () { // лoвим клик пo крестику или oверлэю
        modal // все мoдaльные oкнa
                .animate({opacity: 0, top: '45%'}, 200, // плaвнo прячем
                        function () { // пoсле этoгo
                            $(this).css('display', 'none');
                            overlay.fadeOut(400); // прячем пoдлoжку
                        }
                );
    });
    //});

    $(".expand").click(function () {
        $(this).parent().parent().parent().find(".header").animate({height: "-=75"}, 300);
        $("#logo").animate({height: "hide"}, 200);
        $("#avatar").animate({height: "hide"}, 200);
        $(".options").animate({height: "hide"}, 200);
        $(".toavatar").addClass('todelpad');
        $("#minilogo").animate({height: "show"}, 300);
        $("#miniavatar").animate({height: "show"}, 300);
        $(this).parent().parent().parent().find(".left_side").animate({height: "hide"}, 300);
        $(this).parent().parent().parent().find(".icons").animate({height: "hide"}, 300);
        $(this).parent().parent().parent().find(".icons").addClass("icons_expand");
        $(this).parent().parent().parent().find(".icons").animate({height: "show"}, 300);
        $(this).parent().parent().parent().find(".boxtoitem").addClass("boxtoitembig");
        $(this).parent().find(".rollup").removeClass("invisible");
        $(this).addClass("invisible");

    });
    $(".rollup").click(function () {
        $(this).parent().parent().parent().find(".header").animate({height: "+=75"}, 300);
        $("#minilogo").animate({height: "hide"}, 100);
        $("#miniavatar").animate({height: "hide"}, 100);
        $(".toavatar").removeClass('todelpad');
        $("#logo").animate({height: "show"}, 100);
        $("#avatar").animate({height: "show"}, 100);
        $(".options").animate({height: "show"}, 100);
        $(this).parent().parent().parent().find(".left_side").animate({height: "show"}, 300);
        $(this).parent().parent().parent().find(".icons").animate({height: "hide"}, 300);
        $(this).parent().parent().parent().find(".icons").removeClass("icons_expand");
        $(this).parent().parent().parent().find(".icons").animate({height: "show"}, 300);
        $(this).parent().parent().parent().find(".boxtoitem").removeClass("boxtoitembig");
        $(this).parent().find(".expand").removeClass("invisible");
        $(this).addClass("invisible");
    });


    $('.spoiler_links').click(function () {
        $(this).parent().children('div.spoiler_body').toggle('fast');
        $(this).toggleClass("active");
        return false;
    });
    
    $('.allRegionsSelector').on('change', function () {
        var method = $(this).attr('data-method');
        $('.stateSelector[data-method=' + method + ']').prop('checked', this.checked);
        $('.locSelector[data-method=' + method + ']').prop('checked', this.checked);
        //snyatie
        if (!$(this).prop('checked')) {
            $('.checkedLocsCount[data-method=' + method + ']').html(0);
        //odevanie
        }else{
            $('.stateSelector[data-method=' + method + ']').each(function(id,el){
                var id = $(el).attr('id');
                var count = $('.locSelector[data-state-id=' + id + '][data-method=' + method + ']').length;
                $('.checkedLocsCount[id='+id+'][data-method=' + method + ']').html(count);
            });
            //показать все
            $('.locLabel[data-method=' + method + ']').removeClass('hidden');
        }
        setOpacity(method);
    });

    $('.stateSelector').on('change', function () {
        var method = $(this).attr('data-method');
        var id = $(this).attr('id');
        //odevanie locs
        $('.locSelector[data-state-id=' + id + '][data-method=' + method + ']').prop('checked', this.checked);
        //var countChange = $('.locSelector[data-state-id=' + id + '][data-method=' + method + ']').length;
        //var count = parseInt($('.checkedLocsCount[id='+id+'][data-method=' + method + ']').text());
        var count=0;
        //snyatie
        if (!$(this).prop('checked')) {
            //$('.allRegionsSelector[data-method=' + method + ']').prop('checked', this.checked);
            //count = count-countChange;
        //odevanie
        } else {
            /*var allChecked = true;
            $('.stateSelector[data-method=' + method + ']').each(function () {
                if (!$(this).prop('checked')) {
                    allChecked = false;
                }
            });
            if (allChecked) {
                $('.allRegionsSelector[data-method=' + method + ']').prop('checked', true);
            }*/
            //count = count+countChange;
            count=$('.locSelector[data-state-id=' + id + '][data-method=' + method + ']').length;
        }
        var checkedLocsLength = $('.locSelector[data-method=' + method + ']:checked').length;
        if(checkedLocsLength==0){
            $('.allRegionsSelector[data-method=' + method + ']').prop('checked', false);
        }else{
            $('.allRegionsSelector[data-method=' + method + ']').prop('checked', true);
        }
        $('.checkedLocsCount[id='+id+'][data-method=' + method + ']').html(count);
        setOpacity(method);
    });

    /*$('.locSelector').on('change', function () {
        var method = $(this).attr('data-method');
        var id = $(this).attr('data-state-id');
        var count = parseInt($('.checkedLocsCount[id='+id+'][data-method=' + method + ']').text());
        //снятие
        if (!$(this).prop('checked')) {
            $('.allRegionsSelector[data-method=' + method + ']').prop('checked', this.checked);
            $('.stateSelector[id=' + id + '][data-method=' + method + ']').prop('checked', this.checked);
            count=count-1;
        //одевание
        } else {
            var allChecked = true;
            $('.locSelector[data-state-id=' + id + '][data-method=' + method + ']').each(function () {
                if (!$(this).prop('checked')) {
                    allChecked = false;
                }
            });
            if (allChecked) {
                $('.stateSelector[id=' + id + '][data-method=' + method + ']').prop('checked', true);
                $('.stateSelector[data-method=' + method + ']').each(function () {
                    if (!$(this).prop('checked')) {
                        allChecked = false;
                    }
                });
                if (allChecked) {
                    $('.allRegionsSelector[data-method=' + method + ']').prop('checked', true);
                }
            }
            count=count+1;
        }
        $('.checkedLocsCount[id='+id+'][data-method=' + method + ']').html(count);
        //setOpacity(method);
    });*/
    
    $('.locSelector').on('change', function () {
        var method = $(this).attr('data-method');
        var id = $(this).attr('data-state-id');
        var count = parseInt($('.checkedLocsCount[id='+id+'][data-method=' + method + ']').text());
        //снятие
        if (!$(this).prop('checked')) {
            count=count-1;
        //одевание
        } else {
            count=count+1;
        }
        
        var checkedLocsLength = $('.locSelector[data-method=' + method + ']:checked').length;
        if(checkedLocsLength==0){
            $('.allRegionsSelector[data-method=' + method + ']').prop('checked', false);
        }else{
            $('.allRegionsSelector[data-method=' + method + ']').prop('checked', true);
        }
        
        $('.checkedLocsCount[id='+id+'][data-method=' + method + ']').html(count);
        if(count==0){
            $('.stateSelector[id=' + id + '][data-method=' + method + ']').prop('checked', false);
        }else{
            $('.stateSelector[id=' + id + '][data-method=' + method + ']').prop('checked', true);
        }
        setOpacity(method);
    });

    $(".opener").on('click', function () {
        var method = $(this).attr('data-method');
        var stateId = $(this).attr('id');
        $('.locLabel[data-state-id=' + stateId + '][data-method=' + method + ']').toggleClass('hidden');
    });
    
    $(".allRegionsOpener").on('click', function () {
        var method = $(this).attr('data-method');
        //var allLocs = $('.locLabel[data-method=' + method + ']').length;
        var hiddenLocs = $('.locLabel.hidden[data-method=' + method + ']').length;
        if(hiddenLocs==0){
            $('.locLabel[data-method=' + method + ']').addClass('hidden');
        }else{
            $('.locLabel[data-method=' + method + ']').removeClass('hidden');
        }
    });
    
    function setOpacity(method){
        $('.stateSelector[data-method=' + method + ']').each(function(){
            var checkedLocs = parseInt($(this).siblings('.opener').find('.checkedLocsCount').text());
            var stateLocs = parseInt($(this).siblings('.opener').find('.locsAmount').text());
            if(checkedLocs==stateLocs||checkedLocs==0){
                $(this).removeClass('semichecked');
            }else{
                $(this).addClass('semichecked');
            }
        });
        var allRegsSelector = $('.allRegionsSelector[data-method=' + method + ']')
        var checkedLocs = $('.locSelector[data-method=' + method + ']:checked').length;
        var allLocs = $('.locSelector[data-method=' + method + ']').length;
        if(checkedLocs==0||checkedLocs==allLocs){
            allRegsSelector.removeClass('semichecked');
        }else{
            allRegsSelector.addClass('semichecked');
        }
    }
});
