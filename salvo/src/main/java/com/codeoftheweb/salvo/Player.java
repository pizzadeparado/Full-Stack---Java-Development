package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

@Entity

public class Player {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long id;
  private String userName = "";

  @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
  Set<GamePlayer> gamePlayers;

  public Player() {
  }

  public Player(String user) {
    this.userName = user;
  }

  public String getUserName() {
    return userName;
  }

  public long getPlayerId() {
    return id;
  }

  public String toString() {
    return userName;
  }

}