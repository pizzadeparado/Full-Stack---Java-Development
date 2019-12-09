$(() => loadGrid())

const loadGrid = function (static) {
	var options = {
		width: 10,
		height: 10,
		verticalMargin: 0,
		cellHeight: 45,
		disableResize: true,
		float: true,
		disableOneColumnMode: true,
		staticGrid: static,
		animate: true
	};

	$('.grid-stack').gridstack(options);

	grid = $('#grid').data('gridstack');

	createGrid(11, $(".grid-ships"), "ships")

	grid = $('#grid').data('gridstack');
	grid.addWidget($('<div id="patrol_boat"><div class="grid-stack-item-content patrol_boatHorizontal"></div><div/>'), 0, 1, 2, 1);
	grid.addWidget($('<div id="carrier"><div class="grid-stack-item-content carrierHorizontal"></div><div/>'), 0, 4, 5, 1);
	grid.addWidget($('<div id="battleship"><div class="grid-stack-item-content battleshipHorizontal"></div><div/>'), 3, 1, 4, 1);
	grid.addWidget($('<div id="submarine"><div class="grid-stack-item-content submarineVertical"></div><div/>'), 8, 2, 1, 3);
	grid.addWidget($('<div id="destroyer"><div class="grid-stack-item-content destroyerHorizontal"></div><div/>'), 7, 8, 3, 1);

	setShips();

	listenBusyCells("ships")
	$(".grid-stack").on("change", () => listenBusyCells("ships"))
}

const createGrid = function (size, element, id) {
	let wrapper = document.createElement("div");

	wrapper.classList.add("grid-wrapper");

	for (let i = 0; i < size; i++) {
		let row = document.createElement("div")
		row.classList.add("grid-row")
		row.ID = `${id}-grid-row${i}`
		wrapper.appendChild(row)

		for (let j = 0; j < size; j++) {
			let cell = document.createElement("div")
			cell.classList.add("grid-cell")

			if (i > 0 && j > 0) {
				cell.ID = `${id}${i - 1}${j - 1}`
			}

			if (j === 0 && i > 0) {
				let textNode = document.createElement('span')
				textNode.innerText = String.fromCharCode(i + 64)
				cell.appendChild(textNode)
			}

			if (i === 0 && j > 0) {
				let textNode = document.createElement('span')
				textNode.innerText = j
				cell.appendChild(textNode)
			}
			row.appendChild(cell)
		}
	}
	element.append(wrapper)
};

const setShips = function () {
  for (i = 0; i < salvoGames.ships.length; i++) {
    let shipType = salvoGames.ships[i].shipType.toLowerCase();
    let x = +salvoGames.ships[i].locations[0][1];
    let y = stringToInt[salvoGames.ships[i].locations[0][0].toUpperCase()];
    let w;
    let h;
    let orientation;

    if (
      salvoGames.ships[i].locations[0][0] == salvoGames.ships[i].locations[1][0]
    ) {
      w = salvoGames.ships[i].locations.length;
      h = 1;
      orientation = "Horizontal";
    } else {
      h = salvoGames.ships[i].locations.length;
      w = 1;
      orientation = "Vertical";
    }

    grid.addWidget(
      $(
        '<div id="' +
          shipType +
          '"><div class="grid-stack-item-content ' +
          shipType +
          orientation +
          '"></div><div/>'
      ),
      x,
      y,
      w,
      h
    );
  }
};

const setSalvos = function() {
  var playerOne;
  var playerTwo;
  for (let i = 0; i < salvoGames.players.length; i++) {
    if (salvoGames.players[i].gamePlayerID == gamePlayerID) {
      playerOne = salvoGames.players[i].gamePlayerID;
    } else {
      playerTwo = salvoGames.players[i].gamePlayerID;
    }
  }

  for (i = 0; i < salvoGames.salvos.length; i++) {
    for (j = 0; j < salvoGames.salvos[i].locations.length; j++) {
      let player = salvoGames.salvos[i].game_player_id;
      let x = +salvoGames.salvos[i].locations[j].substring(1);
      let y = stringToInt[salvoGames.salvos[i].locations[j][0].toUpperCase()];

      if (player == playerOne) {
        document
          .getElementById(`salvos${y}${x}`)
          .classList.remove("toBeSalvo");
        document.getElementById(`salvos${y}${x}`).classList.add("sentSalvo");
      } else {
        document.getElementById(`ships` + y + x).classList.add("sentSalvo");
      }
    }
  }
};

const rotateShips = function (shipType, cells) {
	$(`#${shipType}`).click(function () {
		document.getElementById("alert-text").innerHTML = `Rotaste: ${shipType}`;
		console.log($(this));
		let x = +($(this).attr("data-gs-x"));
		let y = +($(this).attr("data-gs-y"));

		if ($(this).children().hasClass(`${shipType}Horizontal`)) {
			if (grid.isAreaEmpty(x, y + 1, 1, cells) || y + cells < 10) {
				if (y + cells - 1 < 10) {
					grid.resize($(this), 1, cells);
					$(this).children().removeClass(`${shipType}Horizontal`);
					$(this).children().addClass(`${shipType}Vertical`);
				} else {
					grid.update($(this), null, 10 - cells);
					grid.resize($(this), 1, cells);
					$(this).children().removeClass(`${shipType}Horizontal`);
					$(this).children().addClass(`${shipType}Vertical`);
				}
			} else {
				document.getElementById("alert-text").innerHTML = "A ship is blocking the way!";
			}

		} else {
			if (x + cells - 1 < 10) {
				grid.resize($(this), cells, 1);
				$(this).children().addClass(`${shipType}Horizontal`);
				$(this).children().removeClass(`${shipType}Vertical`);
			} else {
				grid.update($(this), 10 - cells)
				grid.resize($(this), cells, 1);
				$(this).children().addClass(`${shipType}Horizontal`);
				$(this).children().removeClass(`${shipType}Vertical`);
			}
		}
	});
};

const listenBusyCells = function (id) {
	for (let i = 0; i < 10; i++) {
		for (let j = 0; j < 10; j++) {
			if (!grid.isAreaEmpty(i, j)) {
				$(`#${id}${j}${i}`).addClass('busy-cell').removeClass('empty-cell');
			} else {
				$(`#${id}${j}${i}`).removeClass('busy-cell').addClass('empty-cell');
			}
		}
	}
};

const obtenerPosicion = function (shipType) {
	var ship = new Object();
	ship["name"] = $("#" + shipType).attr('id');
	ship["x"] = $("#" + shipType).attr('data-gs-x');
	ship["y"] = $("#" + shipType).attr('data-gs-y');
	ship["width"] = $("#" + shipType).attr('data-gs-width');
	ship["height"] = $("#" + shipType).attr('data-gs-height');
	ship["positions"] = [];
	if (ship.height == 1) {
		for (i = 1; i <= ship.width; i++) {
			ship.positions.push(String.fromCharCode(parseInt(ship.y) + 65) + (parseInt(ship.x) + i))
		}
	} else {
		for (i = 0; i < ship.height; i++) {
			ship.positions.push(String.fromCharCode(parseInt(ship.y) + 65 + i) + (parseInt(ship.x) + 1))
		}
	}
	var objShip = new Object();
	objShip["type"] = ship.name;
	objShip["shipLocations"] = ship.positions;
	return objShip;
};

/******************** Functions ********************/
function getParameterByName(user) {
	var match = RegExp('[?&]' + user + '=([^&]*)').exec(window.location.search);
	return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
};