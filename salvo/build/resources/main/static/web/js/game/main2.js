const loadGrid = function(static) {
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

  $(".grid-stack").gridstack(options);

  grid = $("#grid").data("gridstack");

  createGrid(11, $(".grid-ships"), "ships");

  setShips();

  listenBusyCells("ships");
  $(".grid-stack").on("change", () => listenBusyCells("ships"));
};

const createGrid = function(size, element, id) {
  let wrapper = document.createElement("DIV");

  wrapper.classList.add("grid-wrapper");

  for (let i = 0; i < size; i++) {
    let row = document.createElement("DIV");
    row.classList.add("grid-row");
    row.id = `${id}-grid-row${i}`;
    wrapper.appendChild(row);

    for (let j = 0; j < size; j++) {
      let cell = document.createElement("DIV");
      cell.classList.add("grid-cell");
      if (i > 0 && j > 0) {
        cell.id = `${id}${i - 1}${j - 1}`;
      }
      if (j === 0 && i > 0) {
        let textNode = document.createElement("SPAN");
        textNode.innerText = String.fromCharCode(i + 64);
        cell.appendChild(textNode);
      }
      if (i === 0 && j > 0) {
        let textNode = document.createElement("SPAN");
        textNode.innerText = j;
        cell.appendChild(textNode);
      }
      /*
            row:
                <div id="ship-grid-row0" class="grid-row">
                    <div class="grid-cell"></div>
                </div>
            */
      row.appendChild(cell);
    }
  }

  element.append(wrapper);
};

const rotateShips = function(shipType, cells) {
  $(`#${shipType}`).click(function() {
    document.getElementById("alert-text").innerHTML = `Rotaste: ${shipType}`;
    console.log($(this));
    //Establecemos nuevos atributos para el widget/barco que giramos
    let x = +$(this).attr("data-gs-x");
    let y = +$(this).attr("data-gs-y");

    if (
      $(this)
        .children()
        .hasClass(`${shipType}Horizontal`)
    ) {
      // grid.isAreaEmpty revisa si un array esta vacio**
      // grid.isAreaEmpty(fila, columna, ancho, alto)
      if (grid.isAreaEmpty(x, y + 1, 1, cells) || y + cells < 10) {
        if (y + cells - 1 < 10) {
          // grid.resize modifica el tamaño de un array(barco en este caso)**
          // grid.resize(elemento, ancho, alto)
          grid.resize($(this), 1, cells);
          $(this)
            .children()
            .removeClass(`${shipType}Horizontal`);
          $(this)
            .children()
            .addClass(`${shipType}Vertical`);
        } else {
          grid.update($(this), null, 10 - cells);
          grid.resize($(this), 1, cells);
          $(this)
            .children()
            .removeClass(`${shipType}Horizontal`);
          $(this)
            .children()
            .addClass(`${shipType}Vertical`);
        }
      } else {
        document.getElementById("alert-text").innerHTML =
          "A ship is blocking the way!";
      }

      //Este bloque se ejecuta si el barco que queremos girar esta en vertical
    } else {
      if (x + cells - 1 < 10) {
        grid.resize($(this), cells, 1);
        $(this)
          .children()
          .addClass(`${shipType}Horizontal`);
        $(this)
          .children()
          .removeClass(`${shipType}Vertical`);
      } else {
        grid.update($(this), 10 - cells);
        grid.resize($(this), cells, 1);
        $(this)
          .children()
          .addClass(`${shipType}Horizontal`);
        $(this)
          .children()
          .removeClass(`${shipType}Vertical`);
      }
    }
  });
};

const listenBusyCells = function(id) {
  for (let i = 0; i < 10; i++) {
    for (let j = 0; j < 10; j++) {
      if (!grid.isAreaEmpty(i, j)) {
        $(`#${id}${j}${i}`)
          .addClass("busy-cell")
          .removeClass("empty-cell");
      } else {
        $(`#${id}${j}${i}`)
          .removeClass("busy-cell")
          .addClass("empty-cell");
      }
    }
  }
};

const setShips = function() {
  for (i = 0; i < gamesJSON.ships.length; i++) {
    //Solo necesito la primera posicion, el resto de la informacion se deduce de la cantidad de celdas
    let shipType = gamesJSON.ships[i].shipType.toLowerCase();
    let x = +gamesJSON.ships[i].locations[0][1];
    let y = stringToInt[gamesJSON.ships[i].locations[0][0].toUpperCase()];
    let w;
    let h;
    let orientation;

    if (
      gamesJSON.ships[i].locations[0][0] == gamesJSON.ships[i].locations[1][0]
    ) {
      w = gamesJSON.ships[i].locations.length;
      h = 1;
      orientation = "Horizontal";
    } else {
      h = gamesJSON.ships[i].locations.length;
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

const setSalvoes = function() {
  var actualPlayer;
  var opponent;
  for (let i = 0; i < gamesJSON.players.length; i++) {
    if (gamesJSON.players[i].gpid == gpId) {
      actualPlayer = gamesJSON.players[i].gpid;
    } else {
      opponent = gamesJSON.players[i].gpid;
    }
  }

  for (i = 0; i < gamesJSON.salvoes.length; i++) {
    for (j = 0; j < gamesJSON.salvoes[i].locations.length; j++) {
      let player = gamesJSON.salvoes[i].game_player_id;
      let x = +gamesJSON.salvoes[i].locations[j].substring(1);
      let y = stringToInt[gamesJSON.salvoes[i].locations[j][0].toUpperCase()];

      if (player == actualPlayer) {
        document
          .getElementById(`salvoes${y}${x}`)
          .classList.remove("toBeSalvo");
        document.getElementById(`salvoes${y}${x}`).classList.add("sentSalvo");
      } else {
        document.getElementById(`ships` + y + x).classList.add("sentSalvo");
      }
    }
  }
};

const getHits = function() {
  var actualPlayer;
  var opponent;
  for (let i = 0; i < gamesJSON.players.length; i++) {
    if (gamesJSON.players[i].gpid == gpId) {
      actualPlayer = gamesJSON.players[i].gpid;
    } else {
      opponent = gamesJSON.players[i].gpid;
    }
  }
  for (i = 0; i < gamesJSON.salvoes.length; i++) {
    for (j = 0; j < gamesJSON.salvoes[i].hits.length; j++) {
      let player = gamesJSON.salvoes[i].game_player_id;
      let x = +gamesJSON.salvoes[i].hits[j][1];
      let y = stringToInt[gamesJSON.salvoes[i].hits[j][0].toUpperCase()];

      if (player == actualPlayer) {
        document
          .getElementById(`salvoes${y}${x}`)
          .classList.remove("sentSalvo");
        document.getElementById(`salvoes${y}${x}`).classList.add("hit");
      } else {
        document
          .getElementById(`ships${y}${x}`)
          .classList.remove("sentSalvo" && "busy-cell");
        document.getElementById(`ships${y}${x}`).classList.add("hit");
      }
    }
  }
};

const intToString = {
  0: "A",
  1: "B",
  2: "C",
  3: "D",
  4: "E",
  5: "F",
  6: "G",
  7: "H",
  8: "I",
  9: "J"
};
const stringToInt = {
  A: 0,
  B: 1,
  C: 2,
  D: 3,
  E: 4,
  F: 5,
  G: 6,
  H: 7,
  I: 8,
  J: 9
};