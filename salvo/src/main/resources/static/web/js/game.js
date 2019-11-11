/******************** Hecho con React ********************/



/******************** Hecho con Vue ********************/


/******************** Viejo Vue ********************/
function getParameterByName(user) {
  var match = RegExp('[?&]' + user + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

$.get("/api/game_view/" + getParameterByName("gamePlayerID"))
  .done(function (data) {
    console.log(data);
    var playerInfo;
    if (data.player[0].playerID == getParameterByName("gamePlayerID")) {
      playerInfo = [data.player[0].user, data.player[1].user];
    }
    else {
      playerInfo = [data.player[1].user, data.player[0].user];
    }

    $('#playerInfo').text(playerInfo[0].user + '(you) vs ' + playerInfo[1].user);

    data.ships.forEach(function (shipPiece) {
      shipPiece.shipLocations.forEach(function (shipLocation) {
        let turnHitted = isHit(shipLocation, data.salvo, playerInfo[0].gamePlayerID)
        if (turnHitted > 0) {
          $('#B_' + shipLocation).addClass('ship-piece-hitted');
          $('#B_' + shipLocation).text(turnHitted);
        }
        else
          $('#B_' + shipLocation).addClass('ship-piece');
      });
    });
    data.salvo.forEach(function (salvo) {
      console.log(salvo);
      if (playerInfo[0].gamePlayerID === salvo.player) {
        salvo.salvoLocations.forEach(function (location) {
          $('#S_' + location).addClass('salvo');
        });
      } else {
        salvo.salvoLocations.forEach(function (location) {
          $('#_' + location).addClass('salvo');
        });
      }
    });
  })
  .fail(function () {
    swal('Failed');
  });


function isHit(shipLocation, salvo, playerID) {
  var hit = 0;
  salvo.forEach(function (salvo) {
    if (salvo.player != playerID)
      salvo.salvoLocations.forEach(function (location) {
        if (shipLocation === location)
          hit = salvo.turn;
      });
  });
  return hit
};

/******************** VARIOUS ACTIONS ********************/
function backToHomepage() {
  swal("Closing game...", {
    closeOnClickOutside: true,
    icon: "info",
    buttons: false,
    timer: 1500,
  });
  window.setTimeout(function () { window.location.href = "http://localhost:8080/web/games.html" }, 1500);
}


$.post("/games/player" + getParameterByName("gamePlayerID") + "/ships", {
  shipType: "",
  shipLocations: ""
}
.done(function() {
  swal()
})
.fail(function() {
  swal()
})
)