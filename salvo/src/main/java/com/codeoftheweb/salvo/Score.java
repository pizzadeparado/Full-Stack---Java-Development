package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Score {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long id;
  private Date endGameDate;
  private float score;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="game_id")
  private Game game;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="player_id")
  private Player player;

  public Score(){}

  public Score(Game game, Player player, float score, Date endGameDate){
    this.score = score;
    this.game = game;
    this.player = player;
    this.endGameDate = endGameDate;
  }

  public float getScore() {
    return score;
  }

  public Date getEndGameDate() {
    return endGameDate;
  }

  public Game getGame() {
    return game;
  }

  public Player getPlayer() {
    return player;
  }
}