package com.codeoftheweb.salvo;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.http.HttpStatus;import org.springframework.http.ResponseEntity;import org.springframework.security.authentication.AnonymousAuthenticationToken;import org.springframework.security.core.Authentication;import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.web.bind.annotation.*;import java.util.*;import java.util.stream.*;@RestController@RequestMapping("/api")public class SalvoController {  @Autowired  private GameRepository gameRepository;  @Autowired  private GamePlayerRepository gamePlayerRepository;  @Autowired  private PlayerRepository playerRepository;  @Autowired  PasswordEncoder passwordEncoder;  //-------------------- api/games --------------------//  @RequestMapping("/games")  public Map<String, Object> getGames(Authentication authentication) {    Map<String, Object> dto = new LinkedHashMap<>();    if (Guest(authentication)) {      dto.put("player", "guest");    } else {      Player player = playerRepository.findByUserName(authentication.getName());      dto.put("player", player.createPlayerDTO());    }    dto.put("games", gameRepository        .findAll()        .stream()        .map(Game::createGameDTO)        .collect(Collectors.toList()));    return dto;  }  private boolean Guest(Authentication authentication) {    return authentication == null || authentication instanceof AnonymousAuthenticationToken;  }  //-------------------- api/players --------------------//  @RequestMapping("/players")  public List<Map<String, Object>> getPlayers() {    return playerRepository        .findAll()        .stream()        .map(Player::createPlayerDTO)        .collect(Collectors.toList());  }/*  @RequestMapping(path = "/games/{id}/players")​  public ResponseEntity<Map<String, Object>> joinGame (Authentication authentication, @PathVariable long id) {    Game game = gameRepository.findById(id).orElse(null);    if (game == null) {      return new ResponseEntity<>(MakeMap("error", "That game doesn't exist"), HttpStatus.FORBIDDEN);    }​    if (Guest(authentication)) {      return new ResponseEntity<>(MakeMap("error", "No player logged in"), HttpStatus.FORBIDDEN);    }​    if  (game.getGamePlayer().stream().count()>1){      return new ResponseEntity<>(MakeMap("error", "Game full"), HttpStatus.FORBIDDEN);    }​    if (game.getGamePlayer().stream().map(gamePlayer -> gamePlayer.getPlayer().getUserName()).collect(Collectors.toList()).contains(authentication.getName())){      return new ResponseEntity<>(MakeMap("error", "You can't join your game"),HttpStatus.FORBIDDEN);    }​    return new ResponseEntity<>(MakeMap("success", "Game started"), HttpStatus.ACCEPTED);  }  */  //-------------------- api/game_view --------------------//  @RequestMapping("/game_view/{gamePlayerID}")  public Map<String, Object> getGameView(@PathVariable Long gamePlayerID) {    GamePlayer gamePlayer = gamePlayerRepository.getOne(gamePlayerID);    Map<String, Object> dto = gamePlayer.getGame().createGameDTO();    dto.put("ship", gamePlayer        .getShip()        .stream()        .map(Ship::createShipDTO));    dto.put("salvo", gamePlayer        .getGame()        .getGamePlayer()        .stream()        .flatMap(gp -> gp.getSalvo().stream().map(Salvo::createSalvoDTO))        .collect(Collectors.toList()));    return dto;  }  //-------------------- api/leaderboard --------------------//  @RequestMapping("/leaderboard")  public List<Map<String, Object>> getLeaderboard() {    return playerRepository        .findAll()        .stream()        .map(Player::createScoreDTO)        .collect(Collectors.toList());  }}  //-------------------- api/login --------------------//  /*  @RequestMapping("/login")  public List<Map<String, Object>> login() {    return gamePlayerRepository  }}   */