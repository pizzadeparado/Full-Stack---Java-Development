var gameData;

$(function () {
  loadData();
});

function getParameterByName(name) {
  var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
};

function loadData() {
  $.get('/api/game_view/' + getParameterByName('gp'))
    .done(function (data) {
      console.log(data)
      gameData = data;
      let playerInfo;
      if (gameData.gamePlayer[0].ID == getParameterByName('gp')) {
        playerInfo1 = [gameData.gamePlayer[0].Player.user, gameData.gamePlayer[1].Player.user];
      } else {
        playerInfo = [gameData.gamePlayer[1].Player.user, gameData.gamePlayer[0].Player.user];
      }

      $('#player1Info').text(playerInfo[0] + '(you)');
      $('#player2Info').text(playerInfo[1]);

      gameData.ships.forEach(function (shipPiece) {
        shipPiece.locations.forEach(function (shipLocation) {
          let turnHitted = isHit(shipLocation, gameData.salvos, playerInfo[0].ID)
          if (turnHitted > 0) {
            $('#B_' + shipLocation).addClass('ship-piece-hitted');
            $('#B_' + shipLocation).text(turnHitted);
          } else
            $('#B_' + shipLocation).addClass('ship-piece');
        });
      });
      gameData.salvos.sort().forEach(function (salvo) {
        console.log(salvo);
        if (playerInfo[0].ID === salvo.player) {
          salvo.locations.forEach(function (location) {
            $('#S_' + location).addClass('salvo');
          });
        } else {
          salvo.locations.forEach(function (location) {
            $('#B_' + location).addClass('salvo');
          });
        }
      });
    })

    .fail(function (jqXHR, textStatus) {
      alert("No user logged in. You need to log in before starting a game.");
      window.location.href = "http://localhost:8080/web/games.html"
    });
}

function isHit(shipLocation, salvos, playerID) {
  var hit = 0;
  salvos.forEach(function (salvo) {
    if (salvo.player != playerID)
      salvo.locations.forEach(function (location) {
        if (shipLocation === location)
          hit = salvo.turn;
      });
  });
  return hit;
}