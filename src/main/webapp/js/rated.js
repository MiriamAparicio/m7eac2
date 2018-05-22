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


    var ratedRest = $("#restaurantsValorats");
    var servletURL = "rated?action=ratedRestList";
    $.ajax({
        type: "GET",
        crossDomain: true,
        dataType: "json",
        async: true,
        url: servletURL,
        success: function (data) {
            var myHtml = renderListRest(data);
            ratedRest.html(myHtml);
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
});
$(document).on('click', '[class*="deleteRest"]', function () {
    var rest = $(this).attr("id");

    var servletURL = "rated?action=deleteRatedRest&rest=" + rest;
    $.ajax({
        type: "GET",
        crossDomain: true,
        dataType: "json",
        async: true,
        url: servletURL,
        success: function (data) {
            window.location.href = "valoracions.html";

        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.info('in error');
            console.log(jqXHR, textStatus, errorThrown);
            alert("You can not send Cross Domain AJAX requests: " + errorThrown);
        }
    });
});



function renderListRest(data) {
    var myHtml = "";
    $.each(data.jsonArray, function (index) {
        myHtml += '<div class="col s12 m12 l12"> <div class="card grey lighten-4 hoverable">';
        myHtml += renderRest(data.jsonArray[index]);
        myHtml += '</div></div>';
    });
    return myHtml;
}

function renderRest(dataRest) {
    //TODO si stock és 0
    var myHtmlP = "";
    var rest = "";
    var med = 0.0;
    var rate = 0;
    var rateList;
    $.each(dataRest, function (key, value) {
        if (key == 'name') {
            rest = value;
        }
        if (key == 'med') {
            med = parseFloat(value);
        }
        if (key == 'rate') {
            rate = parseInt(value);
        }
        if (key == 'rateList') {
            rateList = value;
        }
    });
 
    myHtmlP +='<div class="card-panel grey lighten-5 z-depth-1 hoverable"><div class="row valign-wrapper">';
    myHtmlP += '<div class="col s2"><img  class="circle responsive-img" src="img/' + rest + '.png"/></div>';
    myHtmlP += '<div class="col s10">\n\
                <div class="chip"><h6>Restaurant: <span id="name-' + rest + '">' + rest + '</h6></div>';
    myHtmlP += '<div class="chip"><h6>Mitjana: <span id="med-' + rest + '">' + med + '</h6></div>';
    myHtmlP += '<div class="chip"><h6>Última Valoració: <span id="rate-' + rest + '">' + rate + '</span></h6></div> ';
    myHtmlP += '<div class="chip"><h6>Totes: <span id="list-' + rest + '">' + rateList + '</span></h6></div></div>';
    myHtmlP += '<div class="card-action right-align"><a class ="deleteRest" href="#" id="' + rest + '">Eliminar</a>';
    myHtmlP += '</div></div></div>';
    return myHtmlP;
}

function millorRest(data){
    var dades = ["",0.0];
     $.each(data.jsonArray, function (index) {
        
        var rest = data.jsonArray[index];
        var m = rest["med"];
        if (m > dades[1]){
            dades[0] = rest["name"];
            dades[1] = parseFloat(m);
        }
     });
    
  
    return dades;
}


