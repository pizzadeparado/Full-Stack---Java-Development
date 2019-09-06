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
        .map(game -> game.getDto())
        .collect(Collectors.toList());
  }

  public List<Map<String, Object>> getAllGamePlayers(Set<GamePlayer> gamePlayers) {
    return gamePlayers.stream()
        .map(gamePlayer -> makeGamePlayerD(gamePlayer))
        .collect(Collectors.toList());
  }

}