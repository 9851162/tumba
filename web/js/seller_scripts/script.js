$(document).ready(function () {

    $('#avatarSubmitter').on('mouseover', function () {
        $('#avatarSubmitterButton').attr('style', 'color: #fff;background-color: #3276b1;border-color: #285e8e;');
    });
    $('#avatarSubmitter').on('mouseout', function () {
        $('#avatarSubmitterButton').attr('style', '');
    });

    $('body').on('click', '.messageSender', function () {
        var adId = $(this).attr('data-ad-id');
        $('#msgIdentifier').val(adId);
    });

    $('.categoryChanger').change(function () {
        var catId = $(this).val();
        $('#boxforparams').html($('.catParamsDiv[data-cat-id=' + catId + ']').clone())
        $('body').on('focus', '.paramDatepicker', function () {
            $(this).datepicker({
                dateFormat: "dd.mm.yy"
            });
        });
    });

    $(function () {
        $(".isDatepicker").datepicker();
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


    function addWatch(adId) {
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
                        //alert("При выполнении операции возникла ошибка, ответ сервера не удалось разобрать. Попробуйте обновить страницу и повторить операцию или обратитесь к системному администратору.");
                    }
                }

            },
            error: function (json) {
                alert("Что-то пошло не так: " + json['message']);
            }
        });
    }

    $('body').on('click', '.adChanger', function (event) {
        var method = 'showAd4Ch';
        clearCheckBoxes(method);
        var adId = $(event.target.closest('a')).attr('data-id');
        $('#changeAdForm').find('input[name=formReady]').val('nope');
        $('#changeAdForm').find('button[type=submit]').prop('disabled', 'disabled');
        $.ajax({
            url: "../Ad/getAd?adId=" + adId,
            dataType: "json",
            cache: false,
            success: function (json) {
                if (json['status'] == true) {
                    $.each(json['data'], function (key, value) {
                        if (key == 'catId') {
                            $('#changeAdForm').find('select[name=catId][data-method=changeAd] [value=' + value + ']').attr("selected", "selected");
                        } else if (key == 'params') {
                            var paramArea = '<div class="toin">';
                            paramArea += '<label style="padding-bottom: 3px;font-family: HelveticaNeueThin;font-size: 30px;display: block;width: 100%;">параметры</label><br>';
                            $.each(value, function () {
                                var param = this;
                                var type = param.valtype;
                                var val = param.val;
                                var req = param.req;
                                if (type == "boolean") {
                                    paramArea += '<label>' + param.name + '<select name="' + param.valtype + 'Vals">';
                                    if (req == 0) {
                                        paramArea += '<option value="">не выбрано</option>';
                                    }
                                    paramArea += '<option';
                                    if (val == 1) {
                                        paramArea += ' selected';
                                    }
                                    paramArea += ' value=1>да</option>';
                                    paramArea += '<option';
                                    if (val == 0) {
                                        paramArea += ' selected';
                                    }
                                    paramArea += ' value=0>нет</option>';
                                    paramArea += '</select></label>';
                                } else if (type == "string" || type == "num") {
                                    if (val == undefined) {
                                        val = '';
                                    }
                                    paramArea += '<label>' + param.name + '<input type="text" name="' + param.valtype + 'Vals" placeholder=' + param.name + ' value=' + val + '></label>';
                                } else if (type == "date") {
                                    if (val == undefined) {
                                        val = '';
                                    }
                                    paramArea += '<label>' + param.name + '<input class="adChangeDate" type="text" name="' + param.valtype + 'Vals" placeholder=' + param.name + ' value=' + val + '></label>';
                                } else if (type == "sel") {
                                    paramArea += '<label>' + param.name + '<select name="' + param.valtype + 'Vals">';
                                    if (req == 0) {
                                        paramArea += '<option value="">не выбрано</option>';
                                    }
                                    var opts = param.options;
                                    $.each(opts, function (oid, oname) {
                                        var selected = "";
                                        if (oid == val) {
                                            selected = "selected";
                                        }
                                        paramArea += '<option ' + selected + ' value=' + oid + '>' + oname + '</option>';
                                    });
                                    paramArea += '</select></label>';
                                } else if (type == "multy") {
                                    var opts = param.options;
                                    var size = Object.keys(opts).length;
                                    if (size == undefined || size > 5) {
                                        size = 5;
                                    }
                                    paramArea += '<label>' + param.name + '<select size=' + size + ' multiple name="' + param.valtype + 'Vals">';

                                    $.each(opts, function (oid, oname) {
                                        var selected = "";
                                        if (val != undefined) {
                                            if (val[oid] != undefined) {
                                                selected = "selected";
                                            }
                                        }
                                        paramArea += '<option ' + selected + ' value=' + param.id + '_' + oid + '>' + oname + '</option>';
                                    });

                                    paramArea += '</select></label>';
                                }
                                paramArea += '<input type="hidden" name="' + param.valtype + 'Ids" value=' + param.id + '><br>';
                            });
                            paramArea += '</div>';
                            $('#changeAdForm').find('#boxForChangeAdParams').html(paramArea);
                        } else if (key == 'description') {
                            $('#changeAdForm').find('[name=' + key + ']').text(value);
                        } else if (key == 'locsInReg4ChAd') {
                            $.each(value, function (key1, value1) {
                                $('#changeAdForm').find('#' + key1 + '[type=checkbox][data-method=showAd4Ch][name=localIds]').prop('checked', true);
                            });
                        } else if (key == 'statesInReg4ChAd') {
                            $.each(value, function (key1, value1) {
                                $('#changeAdForm').find('#' + key1 + '[type=checkbox][data-method=showAd4Ch][name=stateIds]').prop('checked', true);
                                $('#changeAdForm').find('#' + key1 + '.checkedLocsCount[data-method=showAd4Ch]').text(value1);
                            });
                        } else if (key == 'status') {
                            if (value != undefined) {
                                var statusSelect = '<div class="toin"><label>статус</label><select name="status">';
                                statusSelect += '<option';
                                if (value == 1) {
                                    statusSelect += ' selected';
                                }
                                statusSelect += ' value="1">ожидание</option>';
                                statusSelect += '<option';
                                if (value == 2) {
                                    statusSelect += ' selected';
                                }
                                statusSelect += ' value="2">оплачено</option>';
                                statusSelect += '<option';
                                if (value == 3) {
                                    statusSelect += ' selected';
                                }
                                statusSelect += ' value="3">доставлено</option>';
                                statusSelect += '</select></div>';
                                $('#changeAdForm').find('[id=statusdiv]').html(statusSelect);
                            }

                        } else {
                            $('#changeAdForm').find('[name=' + key + ']').attr('value', value);
                        }
                    });
                    /*$('#changeAdForm').find('[name=shortName]').attr('value',json['data'].shortName);
                     $('#changeAdForm').find('[name=description]').text(json['data'].description);
                     $('#changeAdForm').find('[name=price]').attr('value',json['data'].price);
                     $('#changeAdForm').find('[name=dateFrom]').attr('value',json['data'].shortName);
                     $('#changeAdForm').find('[name=dateTo]').attr('value',json['data'].shortName);*/
                    $('#changeAdForm').find('input[name=adId]').val(adId);
                    $('#changeAdForm').find('button[type=submit]').prop('disabled', '');
                    $('#changeAdForm').find('input[name=formReady]').val('ready');
                }
                setOpacityAndAllRegSelector(method);
                $('#changeAdForm').on('focus', '.adChangeDate', function () {
                    $(this).datepicker({
                        dateFormat: "dd.mm.yy"
                    });
                });
            }
        });
    });

    $('#changeAdForm').on('change', 'select[name=catId]', function () {
        var catId = $(this).val();
        var adId = $('#changeAdForm input[name=adId]').val();
        $('#changeAdForm').find('input[name=formReady]').val('nope');
        $.ajax({
            url: "../Ad/getAdParams?adId=" + adId + "&catId=" + catId,
            dataType: "json",
            cache: false,
            success: function (json) {
                if (json['status'] == true) {
                    $.each(json['data'], function (key, value) {
                        if (key == 'params') {
                            var paramArea = '<div class="toin">';
                            paramArea += '<label style="padding-bottom: 3px;font-family: HelveticaNeueThin;font-size: 30px;display: block;width: 100%;">параметры</label><br>';
                            $.each(value, function () {
                                var param = this;
                                var type = param.valtype;
                                var val = param.val;
                                var req = param.req;
                                if (type == "boolean") {
                                    paramArea += '<label>' + param.name + '<select name="' + param.valtype + 'Vals">';
                                    if (req == 0) {
                                        paramArea += '<option>не выбрано</option>';
                                    }
                                    paramArea += '<option';
                                    if (val == 1) {
                                        paramArea += ' selected';
                                    }
                                    paramArea += ' value=1>да</option>';
                                    paramArea += '<option';
                                    if (val == 0) {
                                        paramArea += ' selected';
                                    }
                                    paramArea += ' value=0>нет</option>';
                                    paramArea += '</select></label>';
                                } else if (type == "string" || type == "num") {
                                    if (val == undefined) {
                                        val = '';
                                    }
                                    paramArea += '<label>' + param.name + '<input type="text" name="' + param.valtype + 'Vals" placeholder=' + param.name + ' value=' + val + '></label>';
                                } else if (type == "date") {
                                    if (val == undefined) {
                                        val = '';
                                    }
                                    paramArea += '<label>' + param.name + '<input class="adChangeDate" type="text" name="' + param.valtype + 'Vals" placeholder=' + param.name + ' value=' + val + '></label>';
                                } else if (type == "sel") {
                                    paramArea += '<label>' + param.name + '<select name="' + param.valtype + 'Vals">';
                                    if (req == 0) {
                                        paramArea += '<option>не выбрано</option>';
                                    }
                                    var opts = param.options;
                                    $.each(opts, function (oid, oname) {
                                        var selected = "";
                                        if (oid == val) {
                                            selected = "selected";
                                        }
                                        paramArea += '<option ' + selected + ' value=' + oid + '>' + oname + '</option>';
                                    });
                                    paramArea += '</select></label>';
                                } else if (type == "multy") {
                                    var opts = param.options;
                                    var size = Object.keys(opts).length;
                                    if (size == undefined || size > 5) {
                                        size = 5;
                                    }
                                    paramArea += '<label>' + param.name + '<select size=' + size + ' multiple name="' + param.valtype + 'Vals">';

                                    $.each(opts, function (oid, oname) {
                                        var selected = "";
                                        if (val[oid] != undefined) {
                                            selected = "selected";
                                        }
                                        paramArea += '<option ' + selected + ' value=' + param.id + '_' + oid + '>' + oname + '</option>';
                                    });

                                    paramArea += '</select></label>';
                                }
                                paramArea += '<input type="hidden" name="' + param.valtype + 'Ids" value=' + param.id + '><br>';
                            });
                            paramArea += '</div>';
                            $('#changeAdForm').find('#boxForChangeAdParams').html(paramArea);
                            $('#changeAdForm').find('input[name=formReady]').val('ready');
                        }
                    });
                }
            }
        });
        $('#changeAdForm').on('focus', '.adChangeDate', function () {
            $(this).datepicker({
                dateFormat: "dd.mm.yy"
            });
        });
    });

    function clearCheckBoxes(method) {
        $('input[type=checkbox][data-method=' + method + ']').prop('checked', false);
    }



    $('.allRegionsSelector').on('change', function () {
        var method = $(this).attr('data-method');
        $('.stateSelector[data-method=' + method + ']').prop('checked', this.checked);
        $('.locSelector[data-method=' + method + ']').prop('checked', this.checked);
        //snyatie
        if (!$(this).prop('checked')) {
            $('.checkedLocsCount[data-method=' + method + ']').html(0);
            //odevanie
        } else {
            $('.stateSelector[data-method=' + method + ']').each(function (key, el) {
                var id = $(el).attr('id');
                var count = $('.locSelector[data-state-id=' + id + '][data-method=' + method + ']').length;
                $('.checkedLocsCount[data-state-id=' + id + '][data-method=' + method + ']').html(count);
            });
            //показать все
            $('.locLabel[data-method=' + method + ']').removeClass('hidden');
        }
        setOpacityAndAllRegSelector(method);
    });

    $('.stateSelector').on('change', function () {
        var method = $(this).attr('data-method');
        var id = $(this).attr('id');
        //odevanie locs
        $('.locSelector[data-state-id=' + id + '][data-method=' + method + ']').prop('checked', this.checked);
        var count = 0;
        //snyatie
        if (!$(this).prop('checked')) {
            //odevanie
        } else {
            count = $('.locSelector[data-state-id=' + id + '][data-method=' + method + ']').length;
        }
        var checkedLocsLength = $('.locSelector[data-method=' + method + ']:checked').length;
        if (checkedLocsLength == 0) {
            $('.allRegionsSelector[data-method=' + method + ']').prop('checked', false);
        } else {
            $('.allRegionsSelector[data-method=' + method + ']').prop('checked', true);
        }
        $('.checkedLocsCount[data-state-id=' + id + '][data-method=' + method + ']').html(count);
        setOpacityAndAllRegSelector(method);
    });

    $('.locSelector').on('change', function () {
        var method = $(this).attr('data-method');
        var stateId = $(this).attr('data-state-id');
        var count = parseInt($('.checkedLocsCount[data-state-id=' + stateId + '][data-method=' + method + ']').text());
        //снятие
        if (!$(this).prop('checked')) {
            count = count - 1;
            //одевание
        } else {
            count = count + 1;
        }

        var checkedLocsLength = $('.locSelector[data-method=' + method + ']:checked').length;
        if (checkedLocsLength == 0) {
            $('.allRegionsSelector[data-method=' + method + ']').prop('checked', false);
        } else {
            $('.allRegionsSelector[data-method=' + method + ']').prop('checked', true);
        }

        $('.checkedLocsCount[data-state-id=' + stateId + '][data-method=' + method + ']').html(count);
        if (count == 0) {
            $('.stateSelector[id=' + stateId + '][data-method=' + method + ']').prop('checked', false);
        } else {
            $('.stateSelector[id=' + stateId + '][data-method=' + method + ']').prop('checked', true);
        }
        setOpacityAndAllRegSelector(method);
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
        if (hiddenLocs == 0) {
            $('.locLabel[data-method=' + method + ']').addClass('hidden');
        } else {
            $('.locLabel[data-method=' + method + ']').removeClass('hidden');
        }
    });

    function setOpacityAndAllRegSelector(method) {
        $('.stateSelector[data-method=' + method + ']').each(function () {
            var checkedLocs = parseInt($(this).siblings('.opener').find('.checkedLocsCount').text());
            var stateLocs = parseInt($(this).siblings('.opener').find('.locsAmount').text());
            if (checkedLocs == stateLocs || checkedLocs == 0) {
                $(this).removeClass('semichecked');
            } else {
                $(this).addClass('semichecked');
            }
        });
        var allRegsSelector = $('.allRegionsSelector[data-method=' + method + ']')
        var checkedLocs = $('.locSelector[data-method=' + method + ']:checked').length;
        var allLocs = $('.locSelector[data-method=' + method + ']').length;
        if (checkedLocs > 0) {
            allRegsSelector.prop('checked', true);
        } else {
            allRegsSelector.prop('checked', false);
        }
        if (checkedLocs == 0 || checkedLocs == allLocs) {
            allRegsSelector.removeClass('semichecked');
        } else {
            allRegsSelector.addClass('semichecked');
        }
    }

    $('form[action="../Ad/add"] select[name=regionId]').on('change', function () {
        var regId = $('form[action="../Ad/add"] select[name=regionId]').val();
        $('form[action="../Ad/add"]').find('input[name=formReady]').val('nope');
        $.ajax({
            url: "../Regions/getReg?regId=" + regId,
            dataType: "json",
            cache: false,
            success: function (json) {
                var method = "newAd";
                $('.allRegionsSelector[data-method=' + method + ']').prop('checked', false);
                $('.stateSelector[data-method=' + method + ']').prop('checked', false);
                $('.locSelector[data-method=' + method + ']').prop('checked', false);
                if (json['status'] == true) {
                    $.each(json['data'], function (key, value) {
                        if (key == 'locIds') {
                            $.each(value, function (key1, locId) {
                                $('.locSelector[data-method=' + method + '][id=' + locId + ']').prop('checked', true);
                            });
                            setAmountsAndStates(method);
                            setOpacityAndAllRegSelector(method);
                            $('form[action="../Ad/add"]').find('input[name=formReady]').val('ready');
                        }
                    });
                }
            }
        });
    });

    function setAmountsAndStates(method) {
        var states = $('.stateSelector[data-method=' + method + ']');
        $.each(states, function () {
            var stateId = this.id;
            var locsInStateAmount = $('.locSelector[data-method=' + method + '][data-state-id=' + stateId + ']').length;
            var checkedLocsInStateAmount = $('.locSelector[data-method=' + method + '][data-state-id=' + stateId + ']:checked').length;
            $('span.checkedLocsCount[data-state-id=' + stateId + '][data-method=' + method + ']').text(checkedLocsInStateAmount);
            $('span.locsAmount[data-state-id=' + stateId + '][data-method=' + method + ']').text(locsInStateAmount);
            if (checkedLocsInStateAmount > 0) {
                $('.stateSelector[id=' + stateId + '][data-method=' + method + ']').prop('checked', true);
            }
        });
    }

});