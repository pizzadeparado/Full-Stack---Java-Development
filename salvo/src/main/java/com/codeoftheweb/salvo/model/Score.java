package com.codeoftheweb.salvo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;

@Entity
public class Score {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long ID;
  private double score;
  //private Date gameEndDate;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="gameID")
  private Game game;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="playerID")
  private Player player;

  public Score() {}

  public Score(Game game, Player player, double score, Date gameEndDate) {
    this.game = game;
    this.player = player;
    this.score = score;
    //this.gameEndDate = gameEndDate;
  }

  public double getScore() {
    return score;
  }

  //public Date getGameEndDate() {
  //return gameEndDate;
  //}

  @JsonIgnore
  public Game getGame() {
    return game;
  }

  @JsonIgnore
  public Player getPlayer() {
    return player;
  }
}