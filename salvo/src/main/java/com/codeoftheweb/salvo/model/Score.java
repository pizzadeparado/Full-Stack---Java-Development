package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Score {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long ID;
  private LocalDateTime gameEndDate;
  private int points;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="gameID")
  private Game game;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="playerID")
  private Player player;

  public Score() {}

  public Score(int points, Game game, Player player, LocalDateTime gameEndDate) {
    this.points = points;
    this.game = game;
    this.player = player;
    this.gameEndDate = gameEndDate;
  }

 public long getID() {
    return ID;
  }

  public void setID(long ID) {
    this.ID = ID;
  }

  public int getPoints() {
    return this.points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public LocalDateTime getGameEndDate() {
    return gameEndDate;
  }

  public void setGameEndDate(LocalDateTime gameEndDate) {
    this.gameEndDate = gameEndDate;
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public Player getPlayer() {
    return player;
  }

  public void setPlayer(Player player) {
    this.player = player;
  }
}