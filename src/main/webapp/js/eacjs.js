$(document).ready(function () {
    /*
     * MATERIAZLIZE
     */
    $('ul.tabs').tabs('select_tab', 'test4');
    $('.chips').material_chip();
    $('.chips-initial').material_chip({
        data: [{tag: 'Apple'}, {tag: 'Microsoft'}, {tag: 'Google'}]
    });
    $('.chips-placeholder').material_chip({
        placeholder: 'Enter a tag',
        secondaryPlaceholder: '+Tag'
    });
    $('.chips').on('chip.add', function (e, chip) {
        alert("on add");
        alert(chip.tag);
        $('.chips-initial').material_chip('data').forEach(function (untag) {
            alert(untag.tag);
        });
    });

    $('.chips').on('chip.delete', function (e, chip) {
        alert("on delete");
    });

    $('.chips').on('chip.select', function (e, chip) {
        alert("on select");
    });
    $(".button-collapse").sideNav();
    $('.modal-trigger').leanModal();
    /*
     * END MATERIALIZE
     */


    var restaurants = $("#restaurants");
    var servletURL = "Restaurant?action=listRest";
    $.ajax({
        type: "GET",
        crossDomain: true,
        dataType: "json",
        async: true,
        url: servletURL,
        success: function (data) {
            var myHtml = renderListProducts(data);
            restaurants.html(myHtml);
            var dades = millorRest(data);
            $("#bestrest-rate").text(dades[0]);
            $("#bestrest-med").text(dades[1]);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.info('in error');
            console.log(jqXHR, textStatus, errorThrown);
            alert("You can not send Cross Domain AJAX requests: " + errorThrown);
        }
    });

  //p3//
    var servletURL = "user?action=formUser";
    $.ajax({
        type: "GET",
        crossDomain: true,
        dataType: "json",
        async: true,
        url: servletURL,
        success: function (data) {
            $("#username").html("<h5>Valoracions de l'usuari: " + data.user +'</h5>');
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.info('in error');
            console.log(jqXHR, textStatus, errorThrown);
        }
    });


});
$(document).on('click', '[class*="rate"]', function () {
    var restRate = $(this).attr("id").split("-");
    var restaurant = restRate[0];
    var rate = restRate[1];
    var afegir = $(this.parentElement);
    var servletURL = "Restaurant?action=addRateRest&rest=" + restaurant + "&estels=" + rate;
    $.ajax({
        type: "GET",
        crossDomain: true,
        dataType: "json",
        async: true,
        url: servletURL,
        success: function (data) {
            if (data.mitjanaRest == -1.0) {
                $("#mitjana-" + data.valoracio).text('0');
                alert("Sense mitjana. Només una valoració");
            } else {
                
                var myHtml="";
                for (var i = 1; i <= Math.floor(data.valoracio); i++){
                    myHtml += '<img  style="width: 20px;" src="img/star-1.png"/>';
                }
                for (var i = Math.floor(data.valoracio)+1; i <= 5; i++){
                    myHtml += '<img  style="width: 20px;" src="img/star-0.png"/>';
                }
              
                $("#check-" + restaurant).html(myHtml);
                $("#mitjana-" + data.restValorada).text(data.mitjanaRest);
                $("#rate-" + data.restValorada).text(rate);
                
                var bestRestMed = parseFloat($("#bestrest-med").text());
                if(data.mitjanaRest > bestRestMed){
                    $("#bestrest-rate").text(data.restValorada);
                    $("#bestrest-med").text(data.mitjanaRest);
                }                
            }

        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.info('in error');
            console.log(jqXHR, textStatus, errorThrown);
            alert("You can not send Cross Domain AJAX requests: " + errorThrown);
        }
    });
});




function renderListProducts(data) {
    var myHtml = "";
    $.each(data.jsonArray, function (index) {
        myHtml += '<div class="col s12 m3 l3"> <div class="card grey lighten-4 hoverable">';
        myHtml += renderRest(data.jsonArray[index]);
        myHtml += '</div></div>';
    });
    return myHtml;
}

function renderRest(dataRest) {
    var myHtmlP = "";
    var restaurant = "";
    var mitjana = 0.0;
    var valoracio = 0;
    var med = 0.0
    var rated = false;
    $.each(dataRest, function (key, value) {
        if (key == 'name') {
            restaurant = value;
        }
        if (key == 'valoracio') {
            valoracio = parseInt(value);
        }
        if (key == 'mitjana') {
            mitjana = parseFloat(value);
        }
        if (key == 'rated') {
            if (value == 'SI') {
                rated = true;
            } else {
                rated = false;
            }
        }
    });
    myHtmlP += '<div class="card-image"><img src="img/' + restaurant + '.png"/><span class="card-title">' + restaurant + '</span></div>';
    myHtmlP += '<div class="card-content"><div class="chip"><h6>Valoració: <span id="rate-' + restaurant + '">' + valoracio + '</h6></div>';
    myHtmlP += '<div class="chip"><h6>Mitjana: <span id="mitjana-' + restaurant + '">' + mitjana + '</span></h6></div></div>';
    
    if (rated) {
        myHtmlP += '<div class="card-action ">';
        for (var i = 1; i <= Math.floor(valoracio); i++){
            myHtmlP += '<img  style="width: 22px;" src="img/star-1.png"/>';
        }
        for (var i = Math.floor(valoracio)+1; i <= 5; i++){
            myHtmlP += '<img  style="width: 22px;" src="img/star-0.png"/>';
        }
        myHtmlP += '</div>';

    } else {
        myHtmlP += '<div id="check-'+ restaurant+'" class="card-action ">';
       for (var i = 1; i <= 5; i++){
        myHtmlP += '<a class ="rate" href="#" id="' + restaurant + '-' +i+'" style="margin-right:1px;">'+'<img style="width: 22px;" src="img/star-0.png"/>'+'</a>';
       }
        myHtmlP += '</div>';
    }

    return myHtmlP;
}


function millorRest(data){
    var dades = ["",0.0];
     $.each(data.jsonArray, function (index) {
        
        var rest = data.jsonArray[index];
        var m = rest["mitjana"];
        if (m > dades[1]){
            dades[0] = rest["name"];
            dades[1] = parseFloat(m);
        }
     });
    
  
    return dades;
}