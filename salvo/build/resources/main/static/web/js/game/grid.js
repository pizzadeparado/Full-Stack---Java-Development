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

  $('.grid-stack').gridstack(options);
  grid = $('#grid').data('gridstack');

  setShips()
  createGrid(11, $(".grid-ships"), 'ships')
  listenBusyCells('ships')

  $('.grid-stack').on('change', function () {
    listenBusyCells('ships')
  })
}

const createGrid = function (size, element, ID) {
  let wrapper = document.createElement('DIV')
  wrapper.classList.add('grid-wrapper')

  for (let i = 0; i < size; i++) {
    let row = document.createElement('DIV')
    row.classList.add('grid-row')
    row.ID = `${ID}grid-row${i}`
    wrapper.appendChild(row)

    for (let j = 0; j < size; j++) {
      let cell = document.createElement('DIV')
      cell.classList.add('grid-cell')

      if (i > 0 && j > 0)
        cell.ID = `${ID}${i - 1}${j - 1}`

      if (j === 0 && i > 0) {
        let textNode = document.createElement('SPAN')
        textNode.innerText = String.fromCharCode(i + 64)
        cell.appendChild(textNode)
      }

      if (i === 0 && j > 0) {
        let textNode = document.createElement('SPAN')
        textNode.innerText = j
        cell.appendChild(textNode)
      }
      row.appendChild(cell)
    }
  }
  element.append(wrapper)
}

const listenBusyCells = function (ID) {
  for (let i = 0; i < 10; i++) {
    for (let j = 0; j < 10; j++) {
      if (!grid.isAreaEmpty(i, j)) {
        $(`#${id}${j}${i}`).addClass('busy-cell').removeClass('empty-cell')
      } else {
        $(`#${id}${j}${i}`).removeClass('busy-cell').addClass('empty-cell')
      }
    }
  }
}

const setShips = function () {
  for (i = 0; i < data.ships.length; i++) {
    let shipType = (data.ships[i].shipType).toLowerCase()
    let x = +(data.ships[i].shipLocations[0].substring(1)) - 1
    let y = stringToInt(data.ships[i].shipLocations[0][0].toUpperCase())
    let w
    let h
    let orientation

    if (data.ships[i].shipLocations[0][0] == data.ships[i].shipLocations[1][0]) {
      w = data.ships[i].shipLocations.length
      h = 1
      orientation = "Horizontal"
    } else {
      h = data.ships[i].shipLocations.length
      w = 1
      orientation = "Vertical"
    }

    grid.addWidget($('<div id="' + shipType + '"><div class="grid-stack-item-content ' + shipType + orientation + '"></div><div/>'), x, y, w, h);
  }
}

const setSalvos = function () {
  for (i = 0; i < data.salvos.length; i++) {
    for (j = 0; j < data.salvos[i].salvoLocations.length; j++) {
      let turn = data.salvos[i].turn
      let player = data.salvos[i].player
      let x = +(data.salvos[i].salvoLocations[j][1]) - 1
      let y = stringToInt(data.salvos[i].salvoLocations[j][0].toUpperCase())

      if (player == salvoUser.ID) {
        document.getElementById(`salvos${y}${x}`).classList.add('salvo')
        document.getElementById(`salvos${y}${x}`).innerHTML = `<span>${turn}</span>`
      } else {
        if (document.getElementById(`ships${y}${x}`).className.indexOf('busy-cell') != -1) {
          document.getElementById(`ships${y}${x}`).classList.remove('busy-cell')
          document.getElementById(`ships${y}${x}`).classList.add('ship-down')
          document.getElementById(`ships${y}${x}`).innerHTML = `<span>${turn}</span>`
        }
      }
    }
  }
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