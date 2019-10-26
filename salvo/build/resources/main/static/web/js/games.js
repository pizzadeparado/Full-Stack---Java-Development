var app = new Vue({
  el: "#app",
  data: {
    vueGames: [],
    vueScore: [],
    currentUser: ''
  },
  methods: {
    gameReturn(ID) {
      window.location.href = "http://localhost:8080/web/game.html?gp=" + ID;
    },
    joinGame(gameID) {
      $.post("/api/game/" + gameID + "/players")
        .done(function (data) {
          window.location.href = "http://localhost:8080/web/game.html?gp=" + data.gamePlayerID;
        })
        .fail(function () {
          swal("Unable to join game. Reloading", {
            closeOnClickOutside: false,
            buttons: false,
            timer: 3000,
          });
          window.setTimeout(function () { window.location.reload() }, 3000);
        });
    }
  }
});

$(function () {
  loadGames();
  loadScore();
});

function getParameterByName(user) {
  var match = RegExp('[?&]' + user + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}


/******************** GAMES LIST ********************/
function loadGames() {
  $.get("/api/games")
    .done(function (data) {
      app.vueGames = data.games;
      app.currentUser = data.player;
    })
    .fail(function () {
      swal("Failed to load the game list. Reloading.", {
        closeOnClickOutside: false,
        buttons: false,
        timer: 3000,
      });
      window.setTimeout(function () { window.location.reload() }, 3000);
    });
}


/******************** SCORE LIST ********************/
function loadScore() {
  $.get("/api/leaderboard")
    .done(function (data) {
      app.vueScore = data;
    })
    .fail(function () {
      swal("Failed to load the score list. Reloading.", {
        closeOnClickOutside: false,
        buttons: false,
        timer: 3000,
      });
      window.setTimeout(function () { window.location.reload() }, 3000);
    });
}


/******************** CREATE GAME ********************/
function createGame() {
  $.post("/api/games")
    .done(function (data) {
      swal("Starting game in 3, 2, 1 ...", {
        closeOnClickOutside: false,
        buttons: false,
        timer: 3000,
      });
      window.setTimeout(function () { window.location.href = "game.html?gp=" + data.gamePlayerID; }, 3000);
    })
    .fail(function () {
      swal("Game couldn't be created. Please try again.", {
        closeOnClickOutside: false,
        buttons: false,
        timer: 3000,
      });
      window.setTimeout(function () { window.location.reload() }, 3000);
    });
}


/******************** REGISTRATION ********************/
function signUp() {
  $.post("/api/players", {
    username: document.getElementById("registrationEmail").value,
    password: document.getElementById("registrationPassword").value
  })
    .done(function () {
      swal("Account created successfully. Redirecting...", {
        closeOnClickOutside: false,
        buttons: false,
        timer: 3000,
      });
      window.setTimeout(function () { window.location.reload() }, 3000);
    })
    .fail(function () {
      swal("Sorry, we couldn't create your account. Please try again.", {
        closeOnClickOutside: false,
        buttons: false,
        timer: 3000,
      });
      window.setTimeout(function () { window.location.reload() }, 3000);
    });
}


/******************** LOG IN ********************/
function login() {
  $.post("/api/login", {
    username: document.getElementById("loginEmail").value,
    password: document.getElementById("loginPassword").value
  })
    .done(function () {
      swal("You're now logged in.", {
        closeOnClickOutside: false,
        buttons: false,
        timer: 3000,
      });
      window.setTimeout(function () { window.location.reload() }, 3000);
    })
    .fail(function () {
      swal("Incorrect username or password.", {
        closeOnClickOutside: false,
        buttons: false,
        timer: 3000,
      });
      window.setTimeout(function () { window.location.reload() }, 3000);
    });
}

/******************** LOG OUT ********************/
function logout() {
  $.post("/api/logout")
    .done(function () {
      swal("You're now logged out. Redirecting you.", {
        closeOnClickOutside: false,
        buttons: false,
        timer: 3000,
      });
      window.setTimeout(function () { window.location.reload() }, 3000);
    })
    .fail(function () {
      swal("There was a problem and we couldn't log you out. Please try again.", {
        closeOnClickOutside: false,
        buttons: false,
        timer: 3000,
      });
      window.setTimeout(function () { window.location.reload() }, 3000);
    });
}