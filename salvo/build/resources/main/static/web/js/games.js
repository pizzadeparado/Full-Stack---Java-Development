var app = new Vue({
  el: "#app",
  data: {
    players: [],
    games: [],
    currentUser: ""
  }
})

$(function () {
  loadGames();
  loadScore();
});

function getParameterByName(name) {
  var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}


/********** games list **********/

function loadGames() {
  $.get("/api/games")
    .done(function (data) {
      app.games = data.games;
    })
    .fail(function (jqXHR, textStatus) {
      alert('Failed to load: ' + textStatus);
    })
}


/********** score list **********/

function loadScore() {
  $.get("/api/leaderboard")
    .done(function (data) {
      app.players = data;
    })
    .fail(function (jqXHR, textStatus) {
      alert('Failed to load: ' + textStatus);
    });
}


/********** create game **********/

function createGame() {
}


/********** join game **********/

function joinGame(gamePlayerID){
    location.href = "/web/game.html?gp=" + gamePlayerID;
}


/********** sign up **********/

function signUp() {
	var form = document.getElementById("registration");

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
		var form = document.getElementById('login-form')
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