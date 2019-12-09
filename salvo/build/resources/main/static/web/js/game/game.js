var data
var salvoUser
var opponent
var gamePlayerID = getParameterByName("gp")

function getParameterByName(user) {
  var match = RegExp('[?&]' + user + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

fetch("/api/game_view/" + gamePlayerID)
  .then(function (response) {
    return response.json()
  })
  .then(function (json) {
    data = json
    whoIsWho()
    
    if (data.ships.length > 0) {
      loadGrid(true)
    } else {
      loadGrid(false)
    }

    createGrid(11, $(".grid-salvos"), 'salvos')
    setSalvos()
    
    var contador = 0
    $('div[id^="salvos"].grid-cell').click(function () {
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

function whoIsWho() {
  for (i = 0; i < data.games.player.length; i++) {
    if (data.player[i].gamePlayerID == gamePlayerID) {
      salvoUser = data.gamePlayer[i].player
    } else {
      opponent = data.gamePlayer[i].player
    }
  }

  let logger = document.getElementById("logger")
  let wrapper = document.createElement('DIV')
  let p1 = document.createElement('P')
  p1.innerHTML = `Hi ${salvoUser.email}!`
  let p2 = document.createElement('P')
  p2.innerHTML = `your opponent is: ${opponent.email}`
  wrapper.appendChild(p1)
  wrapper.appendChild(p2)
  logger.appendChild(wrapper)
}

function getTurn() {
  var arr = []
  var turn = 0;
  data.salvos.map(function (salvo) {
    if (salvo.player == salvoUser.ID) {
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
  var url = "/api/games/players/" + getParameterByName("gp") + "/salvos"
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