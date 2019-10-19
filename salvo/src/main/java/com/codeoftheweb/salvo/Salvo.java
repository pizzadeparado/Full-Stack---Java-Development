package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

@Entity
public class Salvo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long id;

  @ElementCollection
  @Column(name = "salvoLocation")
  private int turn;
  private Set<String> salvoLocation;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "gamePlayer_id")
  private GamePlayer gamePlayer;

  public Salvo() {
  }

  public Salvo(GamePlayer gamePlayer, int turn, Set<String> salvoLocation) {
    this.gamePlayer = gamePlayer;
    this.turn = turn;
    this.salvoLocation = salvoLocation;
  }

  public long getId() {
    return id;
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
    dto.put("player", this.getGamePlayer().getPlayer().getPlayerId());
    dto.put("salvoLocation", this.getSalvoLocation());
    return dto;
  }
}
