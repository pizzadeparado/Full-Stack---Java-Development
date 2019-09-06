var app = new Vue( {
    el: "#app",
    data: {
        games:[]
    }
});

function loadData() {
    $.get("/api/games")
    .done(function(data) {
        app.games = data;
        console.log(app.games);
    })
    .fail(function( jqXHR, textStatus ) {
      console.log( "Failed: " + textStatus );
    });
  }
  loadData();