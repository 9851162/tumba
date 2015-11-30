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

    /*$('.thumbs img').on('click', function(){
     var gallery = $(this).closest('.toblockimg');
     gallery.find('.largeImage').attr('src',$(this).attr('src').replace('thumb','large')); 
     
     }); */

    $('#allRegionsSelector').on('change', function () {
        //var box = $(this).find('input:checkbox');
        $('.stateSelector').prop('checked', this.checked);
        $('.locSelector').prop('checked', this.checked);
    });

    $('.stateSelector').on('change', function () {
        var id = $(this).attr('id');
        $('.locSelector[data-state-id=' + id + ']').prop('checked', this.checked);
        if (!$(this).prop('checked')) {
            $('#allRegionsSelector').prop('checked', this.checked);
        } else {
            var allChecked = true;
            $('.stateSelector').each(function () {
                if (!$(this).prop('checked')) {
                    allChecked = false;
                }
            });
            if (allChecked) {
                $('#allRegionsSelector').prop('checked', true);
            }
        }

    });

    $('.locSelector').on('change', function () {
        var id = $(this).attr('data-state-id');
        if (!$(this).prop('checked')) {
            $('#allRegionsSelector').prop('checked', this.checked);
            $('.stateSelector[id=' + id + ']').prop('checked', this.checked);
        } else {
            var allChecked = true;
            $('.locSelector[data-state-id=' + id + ']').each(function () {
                if (!$(this).prop('checked')) {
                    allChecked = false;
                }
            });
            if (allChecked) {
                $('.stateSelector[id=' + id + ']').prop('checked', true);
                $('.stateSelector').each(function () {
                    if (!$(this).prop('checked')) {
                        allChecked = false;
                    }
                });
                if (allChecked) {
                    $('#allRegionsSelector').prop('checked', true);
                }
            }
        }
    });

    $(".opener").on('click', function () {
        var stateId = $(this).attr('id');
        $('.locLabel[data-state-id=' + stateId + ']').toggleClass('hidden');
    });
    
    $('#allRegionsSelectorCr8').on('change', function () {
        var method = $(this).attr('data-method');
        $('.stateSelectorCr8[data-method=' + method + ']').prop('checked', this.checked);
        $('.locSelectorCr8[data-method=' + method + ']').prop('checked', this.checked);
        //snyatie
        if (!$(this).prop('checked')) {
            $('.checkedLocsCountCr8[data-method=' + method + ']').html(0);
        //odevanie
        }else{
            $('.stateSelectorCr8[data-method=' + method + ']').each(function(id,el){
                var id = $(el).attr('id');
                var count = $('.locSelectorCr8[data-state-id=' + id + '][data-method=' + method + ']').length;
                $('.checkedLocsCountCr8[id='+id+'][data-method=' + method + ']').html(count);
            });
        }
    });

    $('.stateSelectorCr8').on('change', function () {
        var method = $(this).attr('data-method');
        var id = $(this).attr('id');
        $('.locSelectorCr8[data-state-id=' + id + '][data-method=' + method + ']').prop('checked', this.checked);
        var countChange = $('.locSelectorCr8[data-state-id=' + id + '][data-method=' + method + ']').length;
        var count = parseInt($('.checkedLocsCountCr8[id='+id+'][data-method=' + method + ']').text());
        //snyatie
        if (!$(this).prop('checked')) {
            $('#allRegionsSelectorCr8[data-method=' + method + ']').prop('checked', this.checked);
            count = count-countChange;
        //odevanie
        } else {
            var allChecked = true;
            $('.stateSelectorCr8[data-method=' + method + ']').each(function () {
                if (!$(this).prop('checked')) {
                    allChecked = false;
                }
            });
            if (allChecked) {
                $('#allRegionsSelectorCr8[data-method=' + method + ']').prop('checked', true);
            }
            count = count+countChange;
        }
        $('.checkedLocsCountCr8[id='+id+'][data-method=' + method + ']').html(count);
    });

    $('.locSelectorCr8').on('change', function () {
        var method = $(this).attr('data-method');
        var id = $(this).attr('data-state-id');
        var count = parseInt($('.checkedLocsCountCr8[id='+id+'][data-method=' + method + ']').text());
        //снятие
        if (!$(this).prop('checked')) {
            $('#allRegionsSelectorCr8[data-method=' + method + ']').prop('checked', this.checked);
            $('.stateSelectorCr8[id=' + id + '][data-method=' + method + ']').prop('checked', this.checked);
            count=count-1;
        //одевание
        } else {
            var allChecked = true;
            $('.locSelectorCr8[data-state-id=' + id + '][data-method=' + method + ']').each(function () {
                if (!$(this).prop('checked')) {
                    allChecked = false;
                }
            });
            if (allChecked) {
                $('.stateSelectorCr8[id=' + id + '][data-method=' + method + ']').prop('checked', true);
                $('.stateSelectorCr8[data-method=' + method + ']').each(function () {
                    if (!$(this).prop('checked')) {
                        allChecked = false;
                    }
                });
                if (allChecked) {
                    $('#allRegionsSelectorCr8[data-method=' + method + ']').prop('checked', true);
                }
            }
            count=count+1;
        }
        $('.checkedLocsCountCr8[id='+id+'][data-method=' + method + ']').html(count);
    });

    $(".openerCr8").on('click', function () {
        var method = $(this).attr('data-method');
        var stateId = $(this).attr('id');
        $('.locLabelCr8[data-state-id=' + stateId + '][data-method=' + method + ']').toggleClass('hidden');
    });
    
    $('#allRegionsSelectorCh').on('change', function () {
        //var box = $(this).find('input:checkbox');
        $('.stateSelectorCh').prop('checked', this.checked);
        $('.locSelectorCh').prop('checked', this.checked);
    });

    $('.stateSelectorCh').on('change', function () {
        var id = $(this).attr('id');
        $('.locSelectorCh[data-state-id=' + id + ']').prop('checked', this.checked);
        if (!$(this).prop('checked')) {
            $('#allRegionsSelectorCh').prop('checked', this.checked);
        } else {
            var allChecked = true;
            $('.stateSelectorCh').each(function () {
                if (!$(this).prop('checked')) {
                    allChecked = false;
                }
            });
            if (allChecked) {
                $('#allRegionsSelectorCh').prop('checked', true);
            }
        }

    });
    
    $('.locSelectorCh').on('change', function () {
        var id = $(this).attr('data-state-id');
        if (!$(this).prop('checked')) {
            $('#allRegionsSelectorCh').prop('checked', this.checked);
            $('.stateSelectorCh[id=' + id + ']').prop('checked', this.checked);
        } else {
            var allChecked = true;
            $('.locSelectorCh[data-state-id=' + id + ']').each(function () {
                if (!$(this).prop('checked')) {
                    allChecked = false;
                }
            });
            if (allChecked) {
                $('.stateSelectorCh[id=' + id + ']').prop('checked', true);
                $('.stateSelectorCh').each(function () {
                    if (!$(this).prop('checked')) {
                        allChecked = false;
                    }
                });
                if (allChecked) {
                    $('#allRegionsSelectorCh').prop('checked', true);
                }
            }
        }
    });
    
    $(".openerCh").on('click', function () {
        var stateId = $(this).attr('id');
        $('.locLabelCh[data-state-id=' + stateId + ']').toggleClass('hidden');
    });
    

});
