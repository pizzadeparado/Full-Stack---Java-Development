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

  @Autowired
  private GamePlayerRepository gamePlayerRepository;

  @RequestMapping("/games")
  public Map<String, Object> getAllGames() {
    Map<String, Object> dto = new HashMap<>();
    dto.put("games", gameRepository.findAll().stream().map(Game::createGameDTO).collect(Collectors.toList()));
    return dto;
  }

  @RequestMapping("/game_view/{gamePlayerId}")
  public Map<String,Object> getGameView(@PathVariable Long gamePlayerId) {
    Optional<GamePlayer> gamePlayer = gamePlayerRepository.findById(gamePlayerId);
    if(gamePlayer.isPresent())
      return gamePlayer.get().dto_gameView();
    else
      return null;
  }
}