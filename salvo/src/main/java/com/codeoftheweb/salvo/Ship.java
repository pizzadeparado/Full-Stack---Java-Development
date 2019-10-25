package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

@Entity
public class Ship {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long ID;
  private String shipType;

  @ElementCollection
  @Column(name = "shipLocation")
  private Set<String> shipLocation;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "gamePlayerID")
  private GamePlayer gamePlayer;

  public Ship() {
  }

  public Ship(GamePlayer gamePlayer, String shipType, Set<String> shipLocation) {
    this.gamePlayer = gamePlayer;
    this.shipType = shipType;
    this.shipLocation = shipLocation;
  }

  public GamePlayer getGamePlayer() {
    return gamePlayer;
  }

  public String getShipType() {
    return shipType;
  }

  public Set<String> getShipLocation() {
    return shipLocation;
  }

  public Map<String, Object> createShipDTO() {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("type", this.getShipType());
    dto.put("location", this.getShipLocation());
    return dto;
  }
}