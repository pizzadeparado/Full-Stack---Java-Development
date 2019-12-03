var data
var player
var opponet
var params = new URLSearchParams(location.search)
var gamePlayer = params.get('gamePlayer')
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

getGameData(gamePlayer)

/******************** User ********************/
function getParameterByName(user) {
  var match = RegExp('[?&]' + user + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}


/******************** Functions ********************/
function getGameData(gamePlayerID) {
  document.getElementById("grid-ships").innerHTML = ""
  document.getElementById("grid-salvos").innerHTML = ""

  createGrid(11, document.getElementById('grid-ships'), 'ships')

  fetch(`/api/game_view/${gamePlayerID}`)
    .then(res => {
      if (res.ok) {
        return res.json()

      } else {
        throw new Error(res.statusText)
      }
    })
    .then(json => {
      data = json

      if (data.ships.length > 0) {
        getShips(data.ships)
        createGrid(11, document.getElementById('grid-salvos'), 'salvos')
        document.getElementById("board").innerHTML += '<div class="hide" id="fire"><button class="btn" onclick="">Fire!</button></div>'
        document.getElementById("board").innerHTML += '<div><button class="btn" onclick="gridView(event)">View salvos</button></div>'
      } else {
        document.getElementById("board").innerHTML += '<div><button class="btn" onclick="addShips()">Add Ships</button></div>'
        createShips('carrier', 5, 'horizontal', document.getElementById('dock'), false)
        createShips('battleship', 4, 'horizontal', document.getElementById('dock'), false)
        createShips('submarine', 3, 'horizontal', document.getElementById('dock'), false)
        createShips('destroyer', 3, 'horizontal', document.getElementById('dock'), false)
        createShips('patrol_boat', 2, 'horizontal', document.getElementById('dock'), false)

      }

      data.gamePlayer.forEach(e => {
        if (e.id == gamePlayer) {
          player = e.player
        } else {
          opponent = e.player
        }
      })
      if (data.salvos.length > 0) {
        getsalvos(data.salvos, player.id)
      }
    })
    .catch(error => console.log(error))
}

function getShips(ships) {

  ships.forEach(ship => {

    createShips(ship.type,
      ship.locations.length,
      ship.locations[0][0] == ship.locations[1][0] ? "horizontal" : "vertical",
      document.getElementById(`ships${ship.locations[0]}`),
      true
    )



  })
}

function getsalvos(salvos, playerId) {
  salvos.forEach(salvo => {
    salvo.locations.forEach(loc => {
      if (salvo.player == playerId) {
        var cell = document.getElementById("salvos" + loc)
        cell.style.background = "red"
        cell.innerText = salvo.turn
      } else {
        var cell = document.getElementById("ships" + loc)
        if (cell.classList.contains('busy-cell')) {
          cell.style.background = "red"
        }

      }
    })

  })
}

function addShips() {
  var ships = []

  document.querySelectorAll(".grid-item").forEach(item => {
    var ship = {}

    ship.type = item.id
    ship.locations = []

    if (item.dataset.orientation == "horizontal") {
      for (i = 0; i < item.dataset.length; i++) {
        ship.locations.push(item.dataset.y + (parseInt(item.dataset.x) + i))
      }
    } else {
      for (i = 0; i < item.dataset.length; i++) {
        ship.locations.push(String.fromCharCode(item.dataset.y.charCodeAt() + i) + item.dataset.x)
      }
    }

    ships.push(ship)
  })

  sendShips(ships, gamePlayer)
}

function sendShips(ships, gamePlayerID) {
  var url = '/api/games/players/' + gamePlayerID + '/ships'
  var init = {
    method: 'POST',
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(ships)
  }
  fetch(url, init)
    .then(res => {
      if (res.ok) {
        return res.json()
      } else {
        return Promise.reject(res.json())
      }
    })
    .then(json => {

      getGameData(gamePlayer)
    })
    .catch(error => error)
    .then(error => console.log(error))

}

function shoot(shots, gamePlayerID) {
  var url = '/api/games/players/' + gamePlayerID + '/salvos'
  var init = {
    method: 'POST',
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(shots)
  }
  fetch(url, init)
    .then(res => {
      if (res.ok) {
        return res.json()
      } else {
        return Promise.reject(res.json())
      }
    })
    .then(json => {

      getGameData(gamePlayer)
    })
    .catch(error => error)
    .then(error => console.log(error))

}

function gridView(ev) {
  var text = ev.target.innerText == "View salvos" ? "View Ships" : "View salvos"

  ev.target.innerText = text

  document.querySelectorAll(".grid").forEach(grid => grid.classList.toggle("active"))
  document.getElementById("fire").classList.toggle("hide")
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