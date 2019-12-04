//main function that shoots the gridstack.js framework and loads the grid with the ships
const loadGrid = function (isStatic) {
  var options = {
    //10 x 10 grid
    width: 10,
    height: 10,
    //space between elements (widgets)
    verticalMargin: 0,
    //height of cells
    cellHeight: 45,
    //disables resizing of widgets
    disableResize: true,
    //floating widgets
    float: true,
    //removeTimeout: 100,
    //allows the widget to occupy more than one column
    disableOneColumnMode: true,
    //false allows widget dragging, true denies it
    staticGrid: isStatic,
    //activates animations
    animate: true
  }
  //grid initialization
  $('.grid-stack').gridstack(options);

  grid = $('#grid').data('gridstack');

  setShips()

  createGrid(11, $(".grid-ships"), 'ships')

  listenBusyCells('ships')
  $('.grid-stack').on('change', function () {
    listenBusyCells('ships')
  })
}


//creates the grid structure
const createGrid = function (size, element, id) {

  let wrapper = document.createElement('DIV')
  wrapper.classList.add('grid-wrapper')

  for (let i = 0; i < size; i++) {
    let row = document.createElement('DIV')
    row.classList.add('grid-row')
    row.id = `${id}grid-row${i}`
    wrapper.appendChild(row)

    for (let j = 0; j < size; j++) {
      let cell = document.createElement('DIV')
      cell.classList.add('grid-cell')
      if (i > 0 && j > 0)
        cell.id = `${id}${i - 1}${j - 1}`

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


//adds a listener to the ships, wich shoots its rotation when clicked
//loops over all the grid cells, verifying if they are empty or busy
//loops over all the grid cells, verifying if they are empty or busy

const listenBusyCells = function (id) {
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


//gets the locations of the ships from the back-end
const setShips = function () {

  for (i = 0; i < gamesData.ships.length; i++) {
    //only the first position of a ship is needed. The remaining positions are given by the orientation and the number of cells
    let shipType = (gamesData.ships[i].shipType).toLowerCase()
    let x = +(gamesData.ships[i].shipLocations[0].substring(1)) - 1 //the number of the first position belongs to the x axis. To match the framework structure beginning at 0, we must substract 1 from it
    let y = stringToInt(gamesData.ships[i].shipLocations[0][0].toUpperCase())//the letter of the first position belongs to y axis. In this case we must transform the string to a number, starting from 0.
    let w
    let h
    let orientation

    if (gamesData.ships[i].shipLocations[0][0] == gamesData.ships[i].shipLocations[1][0]) {
      //if the letter of the first position is equal to letter of the second position, the ship orientation is horizontal.
      //Therefore, the width is equal to the length of the location array and the height is equal to 1
      w = gamesData.ships[i].shipLocations.length
      h = 1
      orientation = "Horizontal"
    } else {
      h = gamesData.ships[i].shipLocations.length
      w = 1
      orientation = "Vertical"
    }

    //Finally, the addWidget function adds the ships to the grid
    grid.addWidget($('<div id="' + shipType + '"><div class="grid-stack-item-content ' + shipType + orientation + '"></div><div/>'),
      x, y, w, h);
  }
}

//gets the locations of the salvoes from the back-end
const setSalvoes = function () {
  for (i = 0; i < gamesData.salvoes.length; i++) {
    for (j = 0; j < gamesData.salvoes[i].salvoLocations.length; j++) {
      let turn = gamesData.salvoes[i].turn
      let player = gamesData.salvoes[i].player
      let x = +(gamesData.salvoes[i].salvoLocations[j][1]) - 1
      let y = stringToInt(gamesData.salvoes[i].salvoLocations[j][0].toUpperCase())

      if (player == actualPlayer.id) {
        document.getElementById(`salvoes${y}${x}`).classList.add('salvo')
        document.getElementById(`salvoes${y}${x}`).innerHTML = `<span>${turn}</span>`
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