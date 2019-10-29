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
            icon: "warning",
            buttons: false,
            timer: 2500,
          });
          window.setTimeout(function () { window.location.reload() }, 2500);
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
        icon: "warning",
        buttons: false,
        timer: 2500,
      });
      window.setTimeout(function () { window.location.reload() }, 2500);
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
        icon: "warning",
        buttons: false,
        timer: 2500,
      });
      window.setTimeout(function () { window.location.reload() }, 2500);
    });
}


/******************** CREATE GAME ********************/
function createGame() {
  $.post("/api/games")
    .done(function (data) {
      swal("Starting a new game in 3, 2, 1...", {
        closeOnClickOutside: false,
        icon: "info",
        buttons: false,
        timer: 2500,
      });
      window.setTimeout(function () { window.location.href = "game.html?gp=" + data.gamePlayerID; }, 2500);
    })
    .fail(function () {
      swal("Game couldn't be created. Please try again.", {
        closeOnClickOutside: true,
        icon: "error",
        buttons: false,
        timer: 2500,
      });
      //window.setTimeout(function () { window.location.reload() }, 2500);
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
        icon: "success",
        buttons: false,
        timer: 2500,
      });
      window.setTimeout(function () { window.location.reload() }, 2500);
    })
    .fail(function () {
      swal("Sorry, we couldn't create your account. Please try again.", {
        closeOnClickOutside: true,
        icon: "warning",
        buttons: false,
        timer: 2500,
      });
      //window.setTimeout(function () { window.location.reload() }, 2500);
    });
}


/******************** LOG IN ********************/
function login() {
  $.post("/api/login", {
    username: document.getElementById("loginEmail").value,
    password: document.getElementById("loginPassword").value
  })
    .done(function () {
      swal("You're now logged in. Redirecting you...", {
        closeOnClickOutside: false,
        icon: "success",
        buttons: false,
        timer: 2500,
      });
      window.setTimeout(function () { window.location.reload() }, 2500);
    })
    .fail(function () {
      swal("Incorrect username or password.", {
        closeOnClickOutside: true,
        icon: "error",
        buttons: false,
        timer: 2500,
      });
      //window.setTimeout(function () { window.location.reload() }, 2500);
    });
}

/******************** LOG OUT ********************/
function logout() {
  $.post("/api/logout")
    .done(function () {
      swal("You're now logged out. Redirecting you...", {
        closeOnClickOutside: false,
        icon: "success",
        buttons: false,
        timer: 2500,
      });
      window.setTimeout(function () { window.location.reload() }, 2500);
    })
    .fail(function () {
      swal("There was a problem and we couldn't log you out. Please try again.", {
        closeOnClickOutside: true,
        icon: "error",
        buttons: false,
        timer: 2500,
      });
      window.setTimeout(function () { window.location.reload() }, 2500);
    });
}


/******************** JOIN & RETURN ********************/
function joinGame() {
$.get("/api/games")
	window.setTimeout(function () {
    window.location.href = "http://localhost:8080/web/game.html?gp=" + game.player[0].gamePlayerID;
  }, 2500);
  swal("Joining game. Please wait.", {
    closeOnClickOutside: false,
    icon: "info",
    buttons: false,
    timer: 2500,
  });
}

function returnGame() {
$.get("/api/games")
  window.setTimeout(function () {
    window.location.href = "http://localhost:8080/web/game.html?gp=" + game.player[1].gamePlayerID;
  }, 2500);
  swal("Taking you back to your game. Please wait.", {
    closeOnClickOutside: false,
    icon: "info",
    buttons: false,
    timer: 2500,
  });
}

/******************** VARIOUS ACTIONS ********************/
function newGameAlert() {
  swal("You need to log in before starting a new game!", {
    closeOnClickOutside: true,
    icon: "warning",
    buttons: false,
    timer: 2500,
  })
}

function signUpAfterLogIn() {
  swal("You're already logged in. You can't create a new account!", {
    closeOnClickOutside: true,
    icon: "info",
    buttons: false,
    timer: 2500,
  })
}