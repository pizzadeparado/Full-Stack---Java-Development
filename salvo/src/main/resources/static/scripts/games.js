var gamesJSON = [];

var app = new Vue({
  el: "#app",
  data: {
    vueGames: gamesJSON
  }
});

$.get("/api/games")
  .done(function(games) {
    gamesJSON = games;
    var date;
    gamesJSON.games.forEach(game => {
      date = new Date(game.created);
      game.created = date.toLocaleString();
    });

    app.vueGames = gamesJSON;
  })
  .fail(function(jqXHR, textStatus) {
    showOutput("Failed: " + textStatus);
  });