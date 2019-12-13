var salvo = new Vue({
  el: "#salvo",
  data: {
    salvoGames: [],
    salvoScore: [],
    salvoUser: "",
    registrationEmail: "",
    registrationPassword: "",
    loginEmail: "",
    loginPassword: ""
  },
  methods: {
    registration() {
      if (
        salvo.registrationEmail == "" || salvo.registrationPassword == ""
      )
        swal("Please complete the fields", {
          closeOnClickOutside: true,
          icon: "error",
          buttons: false,
          timer: 2500,
        })

      else {
        $.post("/api/players", {
          username: salvo.registrationEmail,
          password: salvo.registrationPassword
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
          });
      }
    },
    login() {
      if (
        salvo.loginEmail == "" || salvo.loginPassword == ""
      )
        swal("Please complete the fields", {
          closeOnClickOutside: true,
          icon: "error",
          buttons: false,
          timer: 2500,
        })

      else {
        $.post("/api/login", {
          username: salvo.loginEmail,
          password: salvo.loginPassword
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
            swal("Incorrect username or password", {
              closeOnClickOutside: true,
              icon: "error",
              buttons: false,
              timer: 2500,
            });
          })
      }
    },
    logout() {
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
    },
    createGame() {
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
        });
    },
    joinGame(gameID) {
      $.post("/api/games/" + gameID + "/gamePlayers")
        .done(function (data) {
          swal("Joining game...", {
            closeOnClickOutside: false,
            icon: "info",
            buttons: false,
            timer: 1500,
          });
          window.setTimeout(function () {
            window.location.href = "http://localhost:8080/web/game.html?gp=" + ID;
          }, 1500);
        });
    },
    gameReturn(ID) {
      // if (data.hasShips == "Yes") {
      //   swal("Reopening game...", {
      //     closeOnClickOutside: false,
      //     icon: "info",
      //     buttons: false,
      //     timer: 1500,
      //   });
      //   window.setTimeout(function () {
      //     window.location.href = "http://localhost:8080/web/grid.html"
      //   }, 1500);
      // }
      // else {
      swal("Reopening game...", {
        closeOnClickOutside: false,
        icon: "info",
        buttons: false,
        timer: 1500,
      });
      window.setTimeout(function () {
        window.location.href = "http://localhost:8080/web/game.html?gp=" + ID;
      }, 1500);
    },
    newGameAlert() {
      swal("You need to log in before starting a new game!", {
        closeOnClickOutside: true,
        icon: "warning",
        buttons: false,
        timer: 2500,
      })
    },
    registrationAfterLogIn() {
      swal("You're already logged in. You can't create a new account!", {
        closeOnClickOutside: true,
        icon: "info",
        buttons: false,
        timer: 2500,
      })
    }
  },
});

/******************** User ********************/
function getParameterByName(user) {
  var match = RegExp('[?&]' + user + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}


$(function () {
  loadGames();
  loadScore();
});

/******************** Game List ********************/
function loadGames() {
  $.get("/api/games")
    .done(function (data) {
      salvo.salvoGames = data.games;
      salvo.salvoUser = data.player;
    })
    .fail(function () {
      swal("Failed to load the game list. Reloading.", {
        closeOnClickOutside: false,
        icon: "warning",
        buttons: false,
        timer: 2500,
      });
    });
}

/******************** Score List ********************/
function loadScore() {
  $.get("/api/leaderboard")
    .done(function (data) {
      salvo.salvoScore = data;
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