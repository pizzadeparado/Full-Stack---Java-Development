package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.*;

@RestController
@RequestMapping("/api")
public class SalvoController {

  @Autowired
  private GameRepository gameRepository;

  @Autowired
  private GamePlayerRepository gamePlayerRepository;

  @Autowired
  private PlayerRepository playerRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @RequestMapping("/games")
  public Map<String, Object> getAllGames() {
    Map<String, Object> dto = new HashMap<>();
    dto.put("games", gameRepository.findAll().stream().map(Game::createGameDTO).collect(Collectors.toList()));
    return dto;
  }

  @RequestMapping("/game_view/{gamePlayerId}")
  public Map<String,Object> getGameView(@PathVariable Long gamePlayerId) {
    GamePlayer gamePlayer = gamePlayerRepository.getOne(gamePlayerId);
    Map<String, Object> dto = gamePlayer.getGame().createGameDTO();
    List<Map<String, Object>> shipsDto = gamePlayer.getShips().stream().map(Ship::createShipDTO).collect(Collectors.toList());
    dto.put("ships", shipsDto);
    dto.put("salvos", gamePlayer.getGame().getGamePlayers().stream().flatMap(gp -> gp.getSalvos().stream().map(Salvo::createSalvoDTO)).collect(Collectors.toList()));
    return dto;
  }

  @RequestMapping("/leaderboard")
  public List<Map<String, Object>> getScore() {
    return playerRepository.findAll().stream().map(player -> player.createScoreDTO()).collect(Collectors.toList());
  }
}