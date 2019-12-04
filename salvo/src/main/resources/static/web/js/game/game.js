var gamesData
var actualPlayer
var opponent = {
  "email": "Waiting for opponent"
}

//Para obtener el id del gamePlayer colocado como query en la url
var gpId = getParameterByName("gp")
console.log(gpId)

function getParameterByName(name) {
  var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

fetch("/api/game_view/" + gpId)
  .then(function (response) {
    return response.json()
  })
  .then(function (json) {

    gamesData = json
    //Determina el jugador actual y contra quien juega
    WhoIsWho()

    //Cargamos por medio de gridstackla los barcos y chequeamos si ya hay barcos o no para hacer una grilla statica o no
    //(En un futuro se trabajara sin la pagina intermedia de place-ship por lo que de momento esto comparacion esta demas)
    if (gamesData.ships.length > 0) {
      //if true, the grid is initialized in static mode, that is, the ships can't be moved
      loadGrid(true)
    } else {
      //On the contrary, the grid is initialized in dynamic mode, allowing the user to move the ships
      loadGrid(false)
      //A futuro para cargar los salvos por medio de gridstack
      //loadGridSalvo()
    }

    createGrid(11, $(".grid-salvoes"), 'salvoes') //carga la matriz que contendra los salvoes pero sin gridstack.js
    setSalvoes() //carga los salvoes ya guardados
    var contador = 0
    //Una vez cargado los salvoes con createGrid procedemos a establecer una funcion click por cada celda de la siguiente manera
    $('div[id^="salvoes"].grid-cell').click(function () {
      if (!$(this).hasClass("salvo") && !$(this).hasClass("targetCell") && $(".targetCell").length < 5) {
        $(this).addClass("targetCell");
      } else if ($(this).hasClass("targetCell")) {
        $(this).removeClass("targetCell");
      }
    })
  })
  .catch(function (error) {
    console.log(error)
  })

function WhoIsWho() {
  for (i = 0; i < gamesData.gamePlayers.length; i++) {
    if (gamesData.gamePlayers[i].gpid == gpId) {
      actualPlayer = gamesData.gamePlayers[i].player
    } else {
      opponent = gamesData.gamePlayers[i].player
    }
  }

  let logger = document.getElementById("logger")
  let wrapper = document.createElement('DIV')
  let p1 = document.createElement('P')
  p1.innerHTML = `Hi ${actualPlayer.email}!`
  let p2 = document.createElement('P')
  p2.innerHTML = `your opponent is: ${opponent.email}`
  wrapper.appendChild(p1)
  wrapper.appendChild(p2)
  logger.appendChild(wrapper)
}

function getTurn() {
  var arr = []
  var turn = 0;
  gamesData.salvoes.map(function (salvo) {
    if (salvo.player == actualPlayer.id) {
      arr.push(salvo.turn);
    }
  })
  turn = Math.max.apply(Math, arr);

  if (turn == -Infinity) {
    return 1;
  } else {
    return turn + 1;
  }
}

function shoot() {
  var turno = getTurn();
  var locationsToShoot = [];
  $(".targetCell").each(function () {
    let location = $(this).attr("id").substring(7);
    //"00"
    let locationConverted = String.fromCharCode(parseInt(location[0]) + 65) + (parseInt(location[1]) + 1)

    locationsToShoot.push(locationConverted)
  })
  console.log(locationsToShoot)
  var url = "/api/games/players/" + getParameterByName("gp") + "/salvoes"
  $.post({
    url: url,
    data: JSON.stringify({ turn: turno, salvoLocations: locationsToShoot }),
    dataType: "text",
    contentType: "application/json"
  })
    .done(function (response, status, jqXHR) {
      alert("Salvo added: " + response);
      location.reload();
    })
    .fail(function (jqXHR, status, httpError) {
      alert("Failed to add salvo: " + status + " " + httpError);
    })
}


/******************** Actions ********************/
function backToHomepage() {
  swal("Closing game...", {
    closeOnClickOutside: true,
    icon: "info",
    buttons: false,
    timer: 1500,
  });
  window.setTimeout(function () { window.location.href = "http://localhost:8080/web/games.html" }, 1500);
}