var app = new Vue({
  el: "#app",
  data: {
    vueGames: [],
    vueScore: [],
    currentUser: ''
  },
  methods: {
    returnToGame(ID) {
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
      app.vueGames = data.games.player;
      window.location.href = "game.html?gp=" + data.gamePlayerID;
    })
    .fail(function() {
      alert("Game couldn't be created. Please try again.");
    });
}


/********** join game **********/

function joinGame(gamePlayerID){
  location.href = "/web/game.html?gp=" + gamePlayerID;
}


/********** sign up **********/

function signUp() {
	var form = document.getElementByID("registration");

		$.post("/api/players", {
      email: form["registrationEmail"].value,
      password: form["registrationPassword"].value
    })

    .done(function() {
      alert('Account created successfully.');
    })

    .done(function() {
      location.reload;
    })

    .fail(function(jqXHR, textStatus) {
      alert('Failed to create an account: ' + jqXHR.status);
    });
}

/********** login **********/

function login() {
	if (app.currentUser == "guest") {
		var form = document.getElementByID('login-form')
			$.post("/api/login", {
				username: form["username"].value,
				password: form["password"].value
			})

			.done(setTimeout(function() {
				loadUser(); }, 1000))

      .fail(function (jqXHR, textStatus) {
        alert('Failed to login: ' + jqXHR.status);
      });

  } else {
      console.log("")
  }
}

/********** logout **********/

function logout() {
  $.post("/api/logout")
    .done(window.location.replace("game.html"))
    .fail(function (jqXHR, textStatus) {
      alert('Failed: ' + textStatus);
    });
}