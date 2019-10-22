package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

@Entity
public class Salvo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long ID;
  private int turn;

  @ElementCollection
  @Column(name = "salvoLocation")
  private Set<String> salvoLocation;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "gamePlayerID")
  private GamePlayer gamePlayer;

  public Salvo() {

  }

  public Salvo(GamePlayer gamePlayer, int turn, Set<String> salvoLocation) {
    this.gamePlayer = gamePlayer;
    this.turn = turn;
    this.salvoLocation = salvoLocation;
  }

  public long getID() {
    return ID;
  }

  public GamePlayer getGamePlayer() {
    return gamePlayer;
  }

  public int getTurn() {
    return turn;
  }

  public Set<String> getSalvoLocation() {
    return salvoLocation;
  }

  public Map<String, Object> createSalvoDTO (){
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("turn", this.getTurn());
    dto.put("playerID", this.getGamePlayer().getPlayer().getPlayerID());
    dto.put("location", this.getSalvoLocation());
    return dto;
  }
}
