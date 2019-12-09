var salvoGames
var playerOne
var playerTwo = { "user": "Waiting for opponent." }
var gamePlayerID = getParameterByName("gamePlayer")


/******************** User ********************/
function getParameterByName(user) {
  var match = RegExp('[?&]' + user + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

var ships = [
  {
    "type": "carrier",
    "locations": ["A1", "A2", "A3", "A4", "A5"]
  },
  {
    "type": "battleship",
    "locations": ["A10", "B10", "C10", "D10"]
  },
  {
    "type": "submarine",
    "locations": ["C1", "C2", "C3"]
  },
  {
    "type": "destroyer",
    "locations": ["D1", "D2", "D3"]
  },
  {
    "type": "patrol_boat",
    "locations": ["E1", "E2"]
  }
]

/******************** Games ********************/
fetch("/api/game_view/" + gamePlayerID)
  .then(function (response) {
    return response.json();
  })
  .then(function (json) {
    salvoGames = json;
    whoIsWho();

    if (salvoGames.ships.length > 0) {
      //if true, the grid is initialized in static mode, that is, the ships can't be moved
      loadGrid(true)
    } else {
      //On the contrary, the grid is initialized in dynamic mode, allowing the user to move the ships
      loadGrid(false)
      //A futuro para cargar los salvos por medio de gridstack
      //loadGridSalvo()
    }
  });

/******************** Functions ********************/
function whoIsWho() {
  for (i = 0; i < salvoGames.gamePlayer; i++) {
    if (salvoGames.gamePlayer[i].gamePlayerID == gamePlayerID) {
      playerOne = salvoGames.gamePlayer[i].user
    } else {
      playerTwo = salvoGames.gamePlayer[i].user
    }
  }
  let logger = document.getElementById("logger")
  let wrapper = document.createElement("div")
  let p1 = document.createElement("h6")
  p1.innerHTML = `${playerOne} VS`
  let p2 = document.createElement("h6")
  p2.innerHTML = `${playerTwo}`
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
};

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
};

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