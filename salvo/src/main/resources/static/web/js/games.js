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
      .done(function(data) {
        window.location.href = "http://localhost:8080/web/game.html?gp=" + data.gamePlayerID;
      })
      .fail(alert("Unable to join game."));
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


/********** games list **********/

function loadGames() {
  $.get("/api/games")
    .done(function (data) {
      app.vueGames = data.games;
      app.currentUser = data.player;
      })
    .fail(function (jqXHR, textStatus) {
      alert('Failed to load: ' + textStatus);
    })
}


/********** score list **********/

function loadScore() {
  $.get("/api/leaderboard")
    .done(function (data) {
      app.vueScore = data;
    })
    .fail(function (jqXHR, textStatus) {
      alert('Failed to load: ' + textStatus);
    });
}


/********** create game **********/

function createGame() {
  $.post("/api/games")
    .done(function(data) {
      window.location.href = "game.html?gp=" + data.gamePlayerID;
    })
    .fail(function() {
      alert("Game couldn't be created. Please try again.");
    });
}


/********** sign up **********/

function signUp() {
  $.post("/api/players", {
    username: document.getElementById("registrationEmail").value,
    password: document.getElementById("registrationPassword").value
  })
    .done(function() {
      alert("Account created successfully. Redirecting...");
      window.location.reload();
    })
    .fail(function() {
      alert("Failed to create an account.");
    });
}


/********** login **********/

function login() {
  $.post("/api/login", {
    username: document.getElementById("loginEmail").value,
    password: document.getElementById("loginPassword").value
  })
    .done(function() {
      alert("Log in successful.");
      window.location.reload();
    })
    .fail(function() {
      alert("Incorrect username or password.");
    });
}

/********** logout **********/

function logout() {
  $.post("/api/logout")
    .done(window.location.reload())
    .fail(function (jqXHR, textStatus) {
      alert('Failed: ' + textStatus);
    });
}