package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

@Entity

public class GamePlayer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long id;
  private Date joinDate;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="player_id")
  private Player player;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="game_id")
  private Game game;

  public GamePlayer() {
  }

  public GamePlayer(Player player, Game game, Date joinDate) {
    this.player = player;
    this.game = game;
    this.joinDate = joinDate;
  }

  public Date getJoinDate() {
    return joinDate;
  }

  public long getGamePlayerId() {
    return id;
  }

  @JsonIgnore
  public Player getPlayer() {
    return player;
  }

}