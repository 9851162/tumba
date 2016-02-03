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
                            //$('body').addClass('lock');
                            var $bodyWidth = $("body").width();
                            $("body").css({'overflow':"hidden"}).css({'padding-right':($("body").width()-$bodyWidth)});//убираем сколлбар с боди, если есть, и смещаем на него
                    
                });
    });

    close.click(function (event) { // лoвим клик пo крестику или oверлэю
        var id = event.target.getAttribute('id');
        var cl = event.target.getAttribute('class');
        if(id=='overlay'||cl=='modal_close'){
        modal // все мoдaльные oкнa
                .animate({opacity: 0, top: '45%'}, 200, // плaвнo прячем
                        function () { // пoсле этoгo
                            $(this).css('display', 'none');
                            overlay.fadeOut(400); // прячем пoдлoжку
                            
                           // $('body').removeClass('lock');
                            $("body").css({'padding-right':'0'}).css({'overflow':'auto'});//возвращаем скроллбар
                        }
                );
    }
    });

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

    $('body').on('click','.ashow',function () {
        $(this).parent().children('div.abody').toggle('fast');
        $(this).toggleClass("active");
        return false;
    });
    
    var previewWidth = 150, // ширина превью
        previewHeight = 150, // высота превью
        maxFileSize = 3 * 1024 * 1024, // (байт) Максимальный размер файла (2мб)
        selectedFiles = {},// объект, в котором будут храниться выбранные файлы
        queue = [],
        image = new Image(),
        imgLoadHandler,
        isProcessing = false,
        errorMsg, // сообщение об ошибке при валидации файла
        previewPhotoContainer = document.querySelector('#preview-photo'); // контейнер, в котором будут отображаться превью
    
    $('input[type=file][name=previews]').on('change',function(){
        var newFiles = $(this)[0].files; // массив с выбранными файлами
 
        /*for (var i = 0; i < newFiles.length; i++) {
 
            var file = newFiles[i];
 
            // В качестве "ключей" в объекте selectedFiles используем названия файлов
            // чтобы пользователь не мог добавлять один и тот же файл
            // Если файл с текущим названием уже существует в массиве, переходим к следующему файлу
            if (selectedFiles[file.name] != undefined) continue;
 
            // Валидация файлов (проверяем формат и размер)
            if ( errorMsg = validateFile(file) ) {
                alert(errorMsg);
                return;
            }
 
            // Добавляем файл в объект selectedFiles
            selectedFiles[file.name] = file;
            queue.push(file);
 
        }
 
        $(this).val('');
        processQueue(); // запускаем процесс создания миниатюр
        */
        
        var list =""
        for (var i = 0; i < newFiles.length; i++) {
            var file = newFiles[i];
            list+=file.name+"<br>";
        }
        $("#upload-file-info").html(list);
        
        //$("#upload-file-info").html($(this).val());
        //alert(newFiles);
    });
    
    // Валидация выбранного файла (формат, размер)
    var validateFile = function(file)
    {
        if ( !file.type.match(/image\/(jpeg|jpg|png|gif)/) ) {
            return 'Фотография должна быть в формате jpg, png или gif';
        }
 
        if ( file.size > maxFileSize ) {
            return 'Размер фотографии не должен превышать 3 Мб';
        }
    };
 
    var listen = function(element, event, fn) {
        return element.addEventListener(event, fn, false);
    };
 
    // Создание миниатюры
    var processQueue = function()
    {
        // Миниатюры будут создаваться поочередно
        // чтобы в один момент времени не происходило создание нескольких миниатюр
        // проверяем запущен ли процесс
        if (isProcessing) { return; }
 
        // Если файлы в очереди закончились, завершаем процесс
        if (queue.length == 0) {
            isProcessing = false;
            return;
        }
 
        isProcessing = true;
 
        var file = queue.pop(); // Берем один файл из очереди
 
        var li = document.createElement('LI');
        var span = document.createElement('SPAN');
        var spanDel = document.createElement('SPAN');
        var canvas = document.createElement('CANVAS');
        var ctx = canvas.getContext('2d');
 
        span.setAttribute('class', 'img');
        spanDel.setAttribute('class', 'delete');
        spanDel.innerHTML = 'Удалить';
 
        li.appendChild(span);
        li.appendChild(spanDel);
        li.setAttribute('data-id', file.name);
 
        image.removeEventListener('load', imgLoadHandler, false);
 
        // создаем миниатюру
        imgLoadHandler = function() {
            ctx.drawImage(image, 0, 0, previewWidth, previewHeight);
            URL.revokeObjectURL(image.src);
            span.appendChild(canvas);
            isProcessing = false;
            setTimeout(processQueue, 200); // запускаем процесс создания миниатюры для следующего изображения
        };
 
        // Выводим миниатюру в контейнере previewPhotoContainer
        previewPhotoContainer.appendChild(li);
        listen(image, 'load', imgLoadHandler);
        image.src = URL.createObjectURL(file);
 
        // Сохраняем содержимое оригинального файла в base64 в отдельном поле формы
        // чтобы при отправке формы файл был передан на сервер
        /*var fr = new FileReader();
        fr.readAsDataURL(file);
        fr.onload = (function (file) {
            return function (e) {
                $('#preview-photo').append(
                        '<input type="hidden" name="photos[]" value="' + e.target.result + '" data-id="' + file.name+ '">'
                );
            }
        }) (file);*/
    };
 
    // Удаление фотографии
    $(document).on('click', '#preview-photo li span.delete', function() {
        var fileId = $(this).parents('li').attr('data-id');
 
        if (selectedFiles[fileId] != undefined) delete selectedFiles[fileId]; // Удаляем файл из объекта selectedFiles
        $(this).parents('li').remove(); // Удаляем превью
        //$('input[name^=photo][data-id="' + fileId + '"]').remove(); // Удаляем поле с содержимым файла
    });
    
    
});
