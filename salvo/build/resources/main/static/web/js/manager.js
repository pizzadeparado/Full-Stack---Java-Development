$(function() {

  function showOutput(text) {
    $("#output").text(text);
  }

  function loadData() {
    $.get("/rest/players")
    .done(function(salvoData) {
      showOutput(JSON.stringify(salvoData, null, 2));
    })
    .fail(function( jqXHR, textStatus ) {
      showOutput( "Failed: " + textStatus );
    });
  }

  function addPlayer() {
    var name = $("#email").val();
    if (name) {
      postPlayer(name);
    }
  }

  function postPlayer(userName) {
    $.post({
      headers: {
          'Content-Type': 'application/json'
      },
      dataType: "text",
      url: "/rest/players",
      salvoData: JSON.stringify({ "userName": userName })
    })
    .done(function( ) {
      showOutput( "Saved -- Reloading");
      loadData();
    })
    .fail(function( jqXHR, textStatus ) {
      showOutput( "Failed: " + textStatus );
    });
  }

  $("#add_player").on("click", addPlayer);

  loadData();
});