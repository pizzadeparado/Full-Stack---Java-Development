var salvoGames
var playerOne
var playerTwo
var gamePlayerID = getParameterByName("gp")


/******************** Load game information ********************/
const loadSalvoGames = function () {
  fetch("/api/game_view/" + gamePlayerID)
    .then(function (response) {
      return response.json();
    })
    .then(function (json) {
      salvoGames = json
      whoIsWho()

      if (salvoGames.ships.length > 0) {
        loadGrid(true)
      } else {
        loadGrid(false)
      }

      createGrid(11, $(".grid-salvos"), "salvos")
      setSalvos()
      var contador = 0
      $("div[id^='salvoes'].grid-cell").click(function () {
        if (!$(this).hasClass("salvo") && !$(this).hasClass("targetCell") && $(".targetCell").length < 5) {
          $(this).addClass("targetCell");
        } else if ($(this).hasClass("targetCell")) {
          $(this).removeClass("targetCell");
        }
      })

      salvoGames.hits.playerTwo.forEach(function (playTurn) {
        playTurn.location.forEach(function (hitCell) {
          x = +(hitCell.substring(1)) - 1
          y = stringToInt(hitCell[0].toUpperCase())
          cellID = "#salvos" + y + x;
          $(cellID).addClass("hitCell");
        });
      });

      makeGameRecordTable(salvoGames.hits.playerTwo, "gameRecordOppTable");
      makeGameRecordTable(salvoGames.hits.playerOne, "gameRecordSelfTable");
    })
    .catch(function (error) {
      console.log(error)
    })
}

loadSalvoGames()


/******************** Load functions ********************/
function backToHomepage() {
  swal("Closing game...", {
    closeOnClickOutside: true,
    icon: "info",
    buttons: false,
    timer: 1500,
  });
  window.setTimeout(function () { window.location.href = "http://localhost:8080/web/games.html" }, 1500);
}

function getParameterByName(user) {
  var match = RegExp('[?&]' + user + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

function whoIsWho() {
  for (i = 0; i < salvoGames.gamePlayers.length; i++) {
    if (salvoGames.gamePlayers[i].gamePlayerID == gamePlayerID) {
      playerOne = salvoGames.gamePlayers[i].user
    } else {
      playerTwo = salvoGames.gamePlayers[i].user
    }
  }
  let logger = document.getElementById("logger")
  let wrapper = document.createElement("div")
  let p1 = document.createElement("h6")
  p1.innerHTML = `${playerOne}`
  let vs = document.createElement("h2")
  vs.innerHTML = `VS`
  let p2 = document.createElement("h6")
  p2.innerHTML = `${playerTwo}`
  wrapper.appendChild(p1)
  wrapper.appendChild(vs)
  wrapper.appendChild(p2)
  logger.appendChild(wrapper)
}

function getTurn() {
  var arr = []
  var turn = 0;
  salvoGames.salvos.map(function (salvo) {
    if (salvo.player == playerOne.playerID) {
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
    let locationConverted = String.fromCharCode(parseInt(location[0]) + 65) + (parseInt(location[1]) + 1)
    locationsToShoot.push(locationConverted)
  })
  console.log(locationsToShoot)

  var url = "/api/games/gamePlayers/" + getParameterByName("gp") + "/salvos"
  $.post({
    url: url,
    data: JSON.stringify({ turn: turno, salvoLocations: locationsToShoot }),
    dataType: "text",
    contentType: "application/json"
  })
    .done(function () {
      swal("Salvos fired!", {
        closeOnClickOutside: true,
        buttons: false,
        timer: 2500,
      });
      window.setTimeout(function () {
        location.reload();
      }, 1500);
    })
    .fail(function () {
      swal("Salvos couldn't be fired.", {
        closeOnClickOutside: true,
        buttons: false,
        timer: 2500,
      });
    })
}

function makeGameRecordTable(hitsArray, gameRecordTableID) {
  var tableID = "#" + gameRecordTableID + " tbody";
  $(tableID).empty();
  var shipsAfloat = 5;
  var playerTag;
  if (gameRecordTableID == "gameRecordPlayerTwoTable") {
    playerTag = "#opp";
  } if (gameRecordTableID == "gameRecordPlayerOneTable") {
    playerTag = "#";
  }

  hitsArray.forEach(function (playTurn) {
    var hitsReport = "";
    if (playTurn.damages.carrierHits > 0) {
      hitsReport += "Carrier " + addDamagesIcons(playTurn.damages.carrierHits, "hit") + " ";
      if (playTurn.damages.carrier === 5) {
        hitsReport += "SUNK! ";
        shipsAfloat--;
      }
    } if (playTurn.damages.battleshipHits > 0) {
      hitsReport += "Battleship " + addDamagesIcons(playTurn.damages.battleshipHits, "hit") + " ";
      if (playTurn.damages.battleship === 4) {
        hitsReport += "SUNK! ";
        shipsAfloat--;
      }
    } if (playTurn.damages.submarineHits > 0) {
      hitsReport += "Submarine " + addDamagesIcons(playTurn.damages.submarineHits, "hit") + " ";
      if (playTurn.damages.submarine === 3) {
        hitsReport += "SUNK! ";
        shipsAfloat--;
      }
    } if (playTurn.damages.destroyerHits > 0) {
      hitsReport += "Destroyer " + addDamagesIcons(playTurn.damages.destroyerHits, "hit") + " ";
      if (playTurn.damages.destroyer === 3) {
        hitsReport += "SUNK! ";
        shipsAfloat--;
      }
    } if (playTurn.damages.patrolboatHits > 0) {
      hitsReport += "Patrol Boat " + addDamagesIcons(playTurn.damages.patrolboatHits, "hit") + " ";
      if (playTurn.damages.patrolboat === 2) {
        hitsReport += "SUNK! ";
        shipsAfloat--;
      }
    } if (playTurn.missed > 0) {
      hitsReport += "Missed shots " + addDamagesIcons(playTurn.missed, "missed") + " ";
    } if (hitsReport === "") {
      hitsReport = "All salvoes missed! No damages!"
    }

    $('<tr><td class="textCenter">' + playTurn.turn + '</td><td>' + hitsReport + '</td></tr>').prependTo(tableID);

  });
  $('#shipsLeftSelfCount').text(shipsAfloat);
}

function addDamagesIcons(numberOfHits, hitOrMissed) {
  var damagesIcons = "";
  if (hitOrMissed === "missed") {
    for (var i = 0; i < numberOfHits; i++) {
      damagesIcons += "<img class='hitblast' src='img/missed.png'>"
    }
  }
  if (hitOrMissed === "hit") {
    for (var i = 0; i < numberOfHits; i++) {
      damagesIcons += "<img class='hitblast' src='img/redhit.png'>"
    }
  }
  return damagesIcons;
}