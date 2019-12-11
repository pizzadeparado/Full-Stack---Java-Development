package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long ID;
  private LocalDateTime creationDate;

  @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
  private Set<GamePlayer> gamePlayers = new HashSet<>();

  @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
  private Set<Score> scores = new HashSet<>();

  public Game() {
  }

  public Game(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public long getID() {
    return ID;
  }

  public void setID(long ID) {
    this.ID = ID;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public Set<GamePlayer> getGamePlayer() {
    return gamePlayers;
  }

  public void addGamePlayer(GamePlayer gamePlayer) {
    this.gamePlayers.add(gamePlayer);
    gamePlayer.setGame(this);
  }

  public Set<Score> getScores() {
    return this.scores;
  }

  public void addScore(Score score) {
    this.scores.add(score);
    score.setGame(this);
  }

  public List<Player> getPlayer() {
    return gamePlayers.stream().map(GamePlayer::getPlayer).collect(Collectors.toList());
  }

  public Map<String, Object> createGameDTO() {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("gameID", this.getID());
    dto.put("created", this.getCreationDate());
    dto.put("gamePlayers", this.getGamePlayer().stream().map(GamePlayer::createGamePlayerDTO).collect(Collectors.toList()));
    return dto;
  }
}