var gameJSON = [];
var scoreJSON = [];

var app = new Vue({
	el: "#app",
	data: {
		vueSalvo: gameJSON,
		vueScore: scoreJSON
	}
});


//---------- games list ----------//
$.get("/api/games")
.done(function(salvo) {
	gamesJSON = salvo;
	var date;
	gameJSON.salvo.forEach(game => {
		date = new Date(game.created);
		game.created = date.toLocaleString();
	});

	app.vueSalvo = gameJSON;
})

.fail(function(jqXHR, textStatus) {
	showOutput("Failed: " + textStatus);
});


//---------- scores list ----------//
$.get("api/leaderboard")
.done(function(score) {
	score.sort((a, b) => b["total"] - a["total"]);
	app.vueScore = score;
	console.log(app.vueScore);
})

.fail(function(jqXHR, textStatus) {
	showOutput("Failed: " + textStatus);
});


//---------- login and logout ----------//
function login() {
  $.post("/api/login") {
    username: document.getElementById("loginEmail").value,
    password: document.getElementById("loginPassword").value
  })
    .done(function() {
      alert("Logged in.");
      window.location.reload();
    })
    .fail(function() {
      alert("Username or password is wrong. Try again.");
    });
}

function logout() {
  $.post("/api/logout").done(function() {
    console.log("logged out");
  });
}

function signup() {
$.post("/api/signup").done(function())
}