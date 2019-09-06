package com.codeoftheweb.salvo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.*;

@RestController
@RequestMapping("/api")

public class SalvoController {

  @Autowired
  private GameRepository gameRepository;

  @RequestMapping("/games")
  public List<Map<String, Object>> getGames() {
    return gameRepository.findAll()
        .stream()
        .map(Game -> makeGameDTO(Game))
        .collect(Collectors.toList());
  }

  public Map<String, Object> makeGameDTO(Game game) {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", game.getId());
    dto.put("created", game.getGameDate().getTime());
    dto.put("gamePlayers", getAllGamePlayers(game.getGamePlayers()));
    return dto;
  }

  public List<Map<String, Object>> getAllGamePlayers(Set<GamePlayer> gamePlayers) {
    return gamePlayers.stream()
        .map(GamePlayer -> makeGamePlayerDTO(GamePlayer))
        .collect(Collectors.toList());
  }

  public Map<String, Object> makeGamePlayerDTO(GamePlayer gamePlayer) {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", gamePlayer.getGamePlayerId());
    dto.put("player", makePlayerDTO(gamePlayer.getPlayer()));
    return dto;
  }

  public Map<String, Object> makePlayerDTO(Player player) {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", player.getPlayerId());
    dto.put("email", player.getUserName());
    return dto;
  }
}