let data
let player
let opponet
let params = new URLSearchParams(location.search)
let gamePlayer = params.get("gamePlayer")

getGameData(gamePlayer, true)


/******************** User ********************/
function getParameterByName(user) {
  var match = RegExp('[?&]' + user + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}


/******************** Functions ********************/
function getGameData(gamePlayerID, viewShips) {

  document.getElementById("grid-ships").innerHTML = ""
  document.getElementById("grid-salvos").innerHTML = ""

  createGrid(11, document.getElementById("grid-ships"), "ships")

  fetch("/api/game_view/${gamePlayerID}")
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
        createGrid(11, document.getElementById("grid-salvos"), "salvos")
        document.getElementById('grid-ships').classList.add("active")
        document.getElementById('grid-salvos').classList.remove("active")
        document.getElementById("board").innerHTML += '<div class="hide" id="fire"><button class="btn" onclick="readyToShoot()">Fire!</button></div>'
        document.getElementById("board").innerHTML += '<div><button id="grid-view" class="btn" onclick="gridView(event)">View salvos</button></div>'
        if (!viewShips) {
          document.getElementById('grid-view').click()
        }
        target()
      } else {
        document.getElementById("board").innerHTML += '<div><button class="btn" onclick="addShips()">Add Ships</button></div>'
        createShips('carrier', 5, 'horizontal', document.getElementById('dock'), false)
        createShips('battleship', 4, 'horizontal', document.getElementById('dock'), false)
        createShips('submarine', 3, 'horizontal', document.getElementById('dock'), false)
        createShips('destroyer', 3, 'horizontal', document.getElementById('dock'), false)
        createShips('patrol_boat', 2, 'horizontal', document.getElementById('dock'), false)
      }

      data.gamePlayer.forEach(e => {
        if (e.id == gp) {
          player = e.player
        } else {
          opponent = e.player
        }
      })
      if (data.salvos.length > 0) {
        getSalvos(data.salvos, player.id)
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

function getSalvos(salvos, playerId) {
  salvos.forEach(salvo => {
    salvo.locations.forEach(loc => {
      if (salvo.player == playerId) {
        let cell = document.getElementById("salvos" + loc)
        salvo.hits.includes(loc) ? cell.classList.add('hit') : cell.classList.add('water')
        cell.innerText = salvo.turn
      } else {
        let cell = document.getElementById("ships" + loc)
        if (cell.classList.contains('busy-cell')) {
          cell.style.background = "red"
        }
      }
    })

    if (salvo.sunken != null) {
      salvo.sunken.forEach(ship => {
        if (salvo.player == playerId) {
          ship.locations.forEach(loc => {
            let cell = document.getElementById("salvos" + loc)
            cell.classList.add('sunken')
          })
        }
      })
    }
  })
}

let ships = [
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

function addShips() {
  let ships = []

  document.querySelectorAll(".grid-item").forEach(item => {
    let ship = {}
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
  sendShips(ships, gp)
}

function sendShips(ships, gamePlayerId) {
  let url = '/api/games/players/' + gamePlayerId + '/ships'
  let init = {
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
      getGameData(gp, true)
    })
    .catch(error => error)
    .then(error => console.log(error))
}

function shoot(shots, gamePlayerId) {
  let url = '/api/games/players/' + gamePlayerId + '/salvos'
  let init = {
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
      getGameData(gp, false)
    })
    .catch(error => error)
    .then(error => console.log(error))
}

function readyToShoot() {
  let shots = Array.from(document.querySelectorAll('.target')).map(cell => cell.dataset.y + cell.dataset.x)
  shoot(shots, gp)
}

function gridView(ev) {

  let text = ev.target.innerText == "View salvos" ? "View Ships" : "View salvos"
  ev.target.innerText = text
  document.querySelectorAll(".grid").forEach(grid => grid.classList.toggle("active"))
  document.getElementById("fire").classList.toggle("hide")
}

function target() {
  document.querySelectorAll("#grid-salvos.grid-cell").forEach(cell => cell.addEventListener('click', aim))
}

function aim(evt) {
  if (!evt.target.classList.contains('hit')) {
    if (document.querySelectorAll('.target').length < 5) {
      evt.target.classList.toggle('target')
    } else {
      console.log('to many shots')
    }
  } else {
    console.log('You shot here already!')
  }
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