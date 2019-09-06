package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

@Entity

public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long id;
  private Date gameDate;

  @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
  Set<GamePlayer> gamePlayers;

  public Game() {
  }

  public Game(Date gameDate) {
    this.gameDate = gameDate;
  }

  public Date getGameDate() {
    return gameDate;
  }

  public long getId() {
    return id;
  }

  public Set<GamePlayer> getGamePlayers() {
    return gamePlayers;
  }

}