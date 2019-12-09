package com.codeoftheweb.salvo.controller;import com.codeoftheweb.salvo.model.*;import com.codeoftheweb.salvo.repository.*;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.http.HttpStatus;import org.springframework.http.ResponseEntity;import org.springframework.security.authentication.AnonymousAuthenticationToken;import org.springframework.security.core.Authentication;import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.web.bind.annotation.*;import java.util.*;import java.util.stream.*;@RestController@RequestMapping("/api")public class SalvoController {  @Autowired  private GameRepository gameRepository;  @Autowired  private PlayerRepository playerRepository;  @Autowired  private GamePlayerRepository gamePlayerRepository;  @Autowired  private ShipRepository shipRepository;  @Autowired  private PasswordEncoder passwordEncoder;  private Map<String, Object> makeMap(String key, Object value) {    Map<String, Object> map = new HashMap<>();    map.put(key, value);    return map;  }  private boolean Guest(Authentication authentication) {    return authentication == null || authentication instanceof AnonymousAuthenticationToken;  }  private boolean isOutOfRange(Ship ship) {    for (String cell : ship.getShipLocation()) {      if (!(cell instanceof String) || cell.length() < 2) {        return true;      }      char y = cell.substring(0,1).charAt(0);      Integer x;      try {        x = Integer.parseInt(cell.substring(1));      } catch (NumberFormatException e) {        x = 99;      } if (x < 1 || x > 10 || y < 'A' || y > 'J') {        return true;      }    }    return false;  }  //======================================== PUBLIC loadGames ========================================//  @RequestMapping("/games")  public Map<String, Object> loadGames(Authentication authentication) {    Map<String, Object> dto = new LinkedHashMap<>();    if (!this.Guest(authentication)) {      dto.put("player", playerRepository          .findByUserName(authentication.getName())          .createPlayerDTO());    } else {      dto.put("player", "guest");    }    dto.put("games", gameRepository        .findAll()        .stream()        .map(Game::createGameDTO)        .collect(Collectors.toList()));    return dto;  }  //======================================== PUBLIC loadScore ========================================//  @RequestMapping("/leaderboard")  public List<Object> loadScore() {    return playerRepository        .findAll()        .stream()        .map(Player::createScoreDTO)        .collect(Collectors.toList());  }  //======================================== getGameView ========================================//  @RequestMapping("/game_view/{gamePlayerID}")  public ResponseEntity<Map<String, Object>> getGameView(@PathVariable long gamePlayerID, Authentication authentication) {    ResponseEntity<Map<String, Object>> response;    if (Guest(authentication)) {      response = new ResponseEntity<>(makeMap("error", "You must be logged in first."), HttpStatus.UNAUTHORIZED);    } else {      GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerID).orElse(null);      Player player = playerRepository.findByUserName(authentication.getName());      if (gamePlayer == null) {        response = new ResponseEntity<>(makeMap("error", "Game doesn't exist."), HttpStatus.NOT_FOUND);      } else if (gamePlayer.getPlayer().getPlayerID() != player.getPlayerID()) {        response = new ResponseEntity<>(makeMap("error", "This isn't your game."), HttpStatus.UNAUTHORIZED);      } else {        response = new ResponseEntity<>(this.gameViewDTO(gamePlayer), HttpStatus.ACCEPTED);      }    }    return response;  }  private Map<String, Object> gameViewDTO(GamePlayer gamePlayer) {    Map<String, Object> dto = new LinkedHashMap<>();    if (gamePlayer != null) {      dto.put("ID", gamePlayer.getPlayer().getPlayerID());      dto.put("creationDate", gamePlayer.getGame().getCreationDate());      dto.put("gamePlayer", gamePlayer.getGame().getGamePlayer().stream().map(GamePlayer::createGamePlayerDTO));      dto.put("ships", gamePlayer.getShip().stream().map(Ship::createShipDTO));      dto.put("salvos", gamePlayer.getGame().getGamePlayer().stream().flatMap(gp -> gp.getSalvo().stream().map(Salvo::createSalvoDTO)));    } else {      dto.put("error", "Game doesn't exist.");    }    return dto;  }  //======================================== registration ========================================//  @RequestMapping(path = "/players", method = RequestMethod.POST)  public ResponseEntity<Map<String, Object>> registration(@RequestParam String username, @RequestParam String password) {    ResponseEntity<Map<String, Object>> response;    Player player = playerRepository.findByUserName(username);    if (username.isEmpty() || password.isEmpty()) {      response = new ResponseEntity<>(makeMap("error", "You have to choose a username."), HttpStatus.FORBIDDEN);    } else if (player != null) {      response = new ResponseEntity<>(makeMap("error", "Username already exist"), HttpStatus.FORBIDDEN);    } else {      Player newPlayer = playerRepository.save(new Player(username, passwordEncoder.encode(password)));      response = new ResponseEntity<>(makeMap("ID", newPlayer.getPlayerID()), HttpStatus.CREATED);    }    return response;  }  //======================================== createGame ========================================//  @RequestMapping(path = "/games", method = RequestMethod.POST)  public ResponseEntity<Map<String, Object>> createGame(Authentication authentication) {    ResponseEntity<Map<String, Object>> response;    Map<String, Object> dto = new LinkedHashMap<>();    Player player = playerRepository.findByUserName(authentication.getName());    if (!authentication.isAuthenticated()) {      response = new ResponseEntity<>(makeMap("error", "Not authenticated"), HttpStatus.FORBIDDEN);    }    if (player != null) {      Game newGame = gameRepository.save(new Game(new Date()));      GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(player, newGame, new Date()));      response = new ResponseEntity<>(makeMap("gamePlayerID", newGamePlayer.getGamePlayerID()), HttpStatus.CREATED);    } else {      response = new ResponseEntity<>(makeMap("error", "No User Found"), HttpStatus.FORBIDDEN);    }    return response;  }  //======================================== joinGame ========================================//  @RequestMapping(path = "/games/{gameID}/players", method = RequestMethod.POST)  public ResponseEntity<Map<String, Object>> joinGame(Authentication authentication, @PathVariable long gameID) {    ResponseEntity<Map<String, Object>> response;    if (Guest(authentication)) {      response = new ResponseEntity<>(makeMap("error", "User is not authenticated"), HttpStatus.UNAUTHORIZED);    } else {      Game game = gameRepository.findById(gameID).orElse(null);      if (game == null) {        response = new ResponseEntity<>(makeMap("error", "The game doesn't exist."), HttpStatus.NOT_FOUND);      } else if (game.getGamePlayer().size() > 1) {        response = new ResponseEntity<>(makeMap("error", "Game is full."), HttpStatus.FORBIDDEN);      } else {        Player player = playerRepository.findByUserName(authentication.getName());        if (game.getGamePlayer().stream().anyMatch(gamePlayer -> gamePlayer.getPlayer().getPlayerID() == player.getPlayerID())) {          response = new ResponseEntity<>(makeMap("error", "You can't play against yourself!"), HttpStatus.FORBIDDEN);        } else {          GamePlayer newGamePlayer = gamePlayerRepository.save(new GamePlayer(player, game));          response = new ResponseEntity<>(makeMap("gamePlayerID", newGamePlayer.getGamePlayerID()), HttpStatus.CREATED);        }      }    }    return response;  }  //======================================== addShip ========================================//  @RequestMapping(path = "/games/players/{gamePlayerID}/ships", method = RequestMethod.POST)  public ResponseEntity<Map<String, Object>> addShip(Authentication authentication, @PathVariable long gamePlayerID, @RequestBody Set<Ship> ships) {    ResponseEntity<Map<String, Object>> response;    if (Guest(authentication)) {      response = new ResponseEntity<>(makeMap("error", "You must be logged in first."), HttpStatus.UNAUTHORIZED);    } else {      GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerID).orElse(null);      Player player = playerRepository.findByUserName(authentication.getName());      if (gamePlayer == null) {        response = new ResponseEntity<>(makeMap("error", "Game doesn't exist."), HttpStatus.NOT_FOUND);      } else if (gamePlayer.getPlayer().getPlayerID() != player.getPlayerID()) {        response = new ResponseEntity<>(makeMap("error", "This is not your game."), HttpStatus.UNAUTHORIZED);      } else if (gamePlayer.getShip().size() > 0) {        response = new ResponseEntity<>(makeMap("error", "You've got ships!"), HttpStatus.FORBIDDEN);      } else /*(ships == null || ships.size() != 5)*/ {        response = new ResponseEntity<>(makeMap("error", "You must have 5 ships placed."), HttpStatus.FORBIDDEN);      }      /*else {        if (ships.stream().anyMatch(this::outOfRange)) {          response = new ResponseEntity<>(makeMap("error", "Ships are out of range."), HttpStatus.FORBIDDEN);        } else if (ships.stream().anyMatch(this::notConsecutive)) {          response = new ResponseEntity<>(makeMap("error", "Ships are not consecutive."), HttpStatus.FORBIDDEN);        } else if (this.areOverlapped(ships)) {          response = new ResponseEntity<>(makeMap("error", "Ships are overlapping."), HttpStatus.FORBIDDEN);        } else {          ships.forEach(ship -> gamePlayer.addShip(ship));          gamePlayerRepository.save(gamePlayer);          response = new ResponseEntity<>(makeMap("success", "Ships added."), HttpStatus.CREATED);        }      }*/    }    return response;  }  //======================================== addSalvos ========================================//  /*@RequestMapping(path = "/games/players/{gamePlayerID}/salvos", method = RequestMethod.POST)  public ResponseEntity<Map<String, Object>> addSalvo(Authentication authentication, @PathVariable long gamePlayerID, @RequestBody Set<Salvo> salvos) {    ResponseEntity<Map<String, Object>> response;    if(Guest(authentication)) {      response = new ResponseEntity<>(makeMap("error", "You must be logged in first."), HttpStatus.UNAUTHORIZED);    } else {      GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerID).orElse(null);      Player player = playerRepository.findByUserName(authentication.getName());      if (gamePlayer == null) {        response = new ResponseEntity<>(makeMap("error", "Game doesn't exist."), HttpStatus.NOT_FOUND);      } else if (gamePlayer.getPlayer().getPlayerID() != player.getPlayerID()) {        response = new ResponseEntity<>(makeMap("error", "This is not your game."), HttpStatus.UNAUTHORIZED);      } else if (shots.size() != 5) {        response = new ResponseEntity<>(makeMap("error", "Wrong number of shots."), HttpStatus.FORBIDDEN);      } else {        int turn = gamePlayer.getSalvoes().size() + 1;        Salvo salvo = new Salvo(turn,shots);        gamePlayer.addSalvo(salvo);        gamePlayerRepository.save(gamePlayer);        response = new ResponseEntity<>(makeMap("success", "salvo added"), HttpStatus.CREATED);      }    }    return response;  }  */}