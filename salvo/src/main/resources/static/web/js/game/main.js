// $(() => loadGrid())

const loadGrid = function (isStatic) {
	var options = {
		width: 10,
		height: 10,
		verticalMargin: 0,
		cellHeight: 45,
		disableResize: true,
		float: true,
		disableOneColumnMode: true,
		staticGrid: isStatic,
		animate: true
	}

	$(".grid-stack").gridstack(options);

	grid = $("#grid").data("gridstack");

	if (!isStatic) {
		grid.addWidget($('<div id="patrol_boat"><div class="grid-stack-item-content patrol_boatHorizontal"></div><div/>'), 0, 1, 2, 1);
		grid.addWidget($('<div id="carrier"><div class="grid-stack-item-content carrierHorizontal"></div><div/>'), 0, 4, 5, 1);
		grid.addWidget($('<div id="battleship"><div class="grid-stack-item-content battleshipHorizontal"></div><div/>'), 3, 1, 4, 1);
		grid.addWidget($('<div id="submarine"><div class="grid-stack-item-content submarineVertical"></div><div/>'), 8, 2, 1, 3);
		grid.addWidget($('<div id="destroyer"><div class="grid-stack-item-content destroyerHorizontal"></div><div/>'), 7, 8, 3, 1);

		rotateShips("carrier", 5)
		rotateShips("battleship", 4)
		rotateShips("submarine", 3)
		rotateShips("destroyer", 3)
		rotateShips("patrol_boat", 2)
	} else {
		setShips()
	}

	createGrid(11, $(".grid-ships"), "ships")

	listenBusyCells("ships")
	$(".grid-stack").on("change", function () {
		listenBusyCells("ships")
	})
}

const setShips = function () {
	for (i = 0; i < salvoGames.ships.length; i++) {
		let shipType = (salvoGames.ships[i].shipType).toLowerCase()
		let x = +(salvoGames.ships[i].shipLocation[0].substring(1)) - 1
		let y = stringToInt(salvoGames.ships[i].shipLocation[0][0].toUpperCase())
		let w
		let h
		let orientation
		if (salvoGames.ships[i].shipLocation[0][0] == salvoGames.ships[i].shipLocation[1][0]) {
			w = salvoGames.ships[i].shipLocation.length
			h = 1
			orientation = "Horizontal"
		} else {
			h = salvoGames.ships[i].shipLocation.length
			w = 1
			orientation = "Vertical"
		}
		grid.addWidget($("<div id='" + shipType + "'><div class='grid-stack-item-content " + shipType + orientation + "'></div></div>"), x, y, w, h);
	}
}

const createGrid = function (size, element, id) {
	let wrapper = document.createElement("DIV")
	wrapper.classList.add("grid-wrapper")

	for (let i = 0; i < size; i++) {
		let row = document.createElement("DIV")
		row.classList.add("grid-row")
		row.id = `${id}-grid-row${i}`
		wrapper.appendChild(row)

		for (let j = 0; j < size; j++) {
			let cell = document.createElement("DIV")
			cell.classList.add("grid-cell")
			if (i > 0 && j > 0) {
				cell.id = `${id}${i - 1}${j - 1}`
			} if (j === 0 && i > 0) {
				let textNode = document.createElement("SPAN")
				textNode.innerText = String.fromCharCode(i + 64)
				cell.appendChild(textNode)
			} if (i === 0 && j > 0) {
				let textNode = document.createElement("SPAN")
				textNode.innerText = j
				cell.appendChild(textNode)
			}
			row.appendChild(cell)
		}
	}
	element.append(wrapper)
}

const rotateShips = function (shipType, cells) {
	$(`#${shipType}`).click(function () {
		console.log($(this))
		let x = +($(this).attr("data-gs-x"))
		let y = +($(this).attr("data-gs-y"))

		if ($(this).children().hasClass(`${shipType}Horizontal`)) {
			if (grid.isAreaEmpty(x, y + 1, 1, cells) || y + cells < 10) {
				if (y + cells - 1 < 10) {
					grid.resize($(this), 1, cells);
					$(this).children().removeClass(`${shipType}Horizontal`);
					$(this).children().addClass(`${shipType}Vertical`);
				} else {
					grid.update($(this), null, 10 - cells)
					grid.resize($(this), 1, cells);
					$(this).children().removeClass(`${shipType}Horizontal`);
					$(this).children().addClass(`${shipType}Vertical`);
				}
			} else {
				document.getElementById("alert-text").innerHTML = "A ship is blocking the way!"
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
	})
}

const listenBusyCells = function (id) {
	for (let i = 0; i < 10; i++) {
		for (let j = 0; j < 10; j++) {
			if (!grid.isAreaEmpty(i, j)) {
				$(`#${id}${j}${i}`).addClass("busy-cell").removeClass("empty-cell")
			} else {
				$(`#${id}${j}${i}`).removeClass("busy-cell").addClass("empty-cell")
			}
		}
	}
}

const obtenerPosicion = function (shipType) {
	var ship = new Object();
	ship["name"] = $("#" + shipType).attr("id");
	ship["x"] = $("#" + shipType).attr("data-gs-x");
	ship["y"] = $("#" + shipType).attr("data-gs-y");
	ship["width"] = $("#" + shipType).attr("data-gs-width");
	ship["height"] = $("#" + shipType).attr("data-gs-height");
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
	objShip["shipType"] = ship.name;
	objShip["shipLocation"] = ship.positions;
	return objShip;
}

function addShips() {
	var carrier = obtenerPosicion("carrier")
	var patrol = obtenerPosicion("patrol_boat")
	var battleship = obtenerPosicion("battleship")
	var submarine = obtenerPosicion("submarine")
	var destroyer = obtenerPosicion("destroyer")

	$.post({
		url: "/api/games/players/" + gamePlayerID + "/ships",
		data: JSON.stringify([carrier, patrol, battleship, submarine, destroyer]),
		dataType: "text",
		contentType: "application/json"
	})
		.done(function () {
			swal("Ships have been placed.", {
				closeOnClickOutside: true,
				buttons: false,
				timer: 1500,
			});
			window.setTimeout(function () {
				window.location.href = "/web/game.html?gp=" + gamePlayerID;
			}, 1500);
		})
		.fail(function () {
			swal("Fail to add the ships. Try again.", {
				closeOnClickOutside: true,
				buttons: false,
				timer: 1500,
			})
		});
}

const stringToInt = function (str) {
	switch (str) {
		case "A":
			return 0;
		case "B":
			return 1;
		case "C":
			return 2;
		case "D":
			return 3;
		case "E":
			return 4;
		case "F":
			return 5;
		case "G":
			return 6;
		case "H":
			return 7;
		case "I":
			return 8;
		case "J":
			return 9;
	}
}

const setSalvos = function () {
	for (i = 0; i < salvoGames.salvos.length; i++) {
		for (j = 0; j < salvoGames.salvos[i].salvoLocation.length; j++) {
			let turn = salvoGames.salvos[i].turn
			let player = salvoGames.salvos[i].player
			let x = +(salvoGames.salvos[i].salvoLocation[j].substring(1)) - 1
			let y = stringToInt(salvoGames.salvos[i].salvoLocation[j][0].toUpperCase())
			if (player == playerOne.ID) {
				document.getElementById(`salvos${y}${x}`).classList.add("salvos")
				document.getElementById(`salvos${y}${x}`).innerHTML = `<span>${turn}</span>`
			} else {
				if (document.getElementById(`ships${y}${x}`).className.indexOf("busy-cell") != -1) {
					document.getElementById(`ships${y}${x}`).classList.remove("busy-cell")
					document.getElementById(`ships${y}${x}`).classList.add("ship-down")
					document.getElementById(`ships${y}${x}`).innerHTML = `<span>${turn}</span>`
				}
			}
		}
	}
}

function getParameterByName(name) {
	var match = RegExp("[?&]" + name + "=([^&]*)").exec(window.location.search);
	return match && decodeURIComponent(match[1].replace(/\+/g, " "));
}


function backToHomepage() {
	swal("Closing game...", {
		closeOnClickOutside: true,
		icon: "info",
		buttons: false,
		timer: 1500,
	});
	window.setTimeout(function () { window.location.href = "http://localhost:8080/web/games.html" }, 1500);
}