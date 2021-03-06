package com.codeoftheweb.salvo.model;

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
  private List<String> shipLocation;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "gamePlayerID")
  private GamePlayer gamePlayer;

  public Ship() {
  }

  public Ship(GamePlayer gamePlayer, String shipType, List<String> shipLocation) {
    this.gamePlayer = gamePlayer;
    this.shipType = shipType;
    this.shipLocation = shipLocation;
  }

  public long getID() {
    return ID;
  }

  public GamePlayer getGamePlayer() {
    return gamePlayer;
  }

  public void setGamePlayer(GamePlayer gamePlayer) {
    this.gamePlayer = gamePlayer;
  }

  public String getShipType() {
    return shipType;
  }

  public void setShipType(String shipType) {
    this.shipType = shipType;
  }

  public List<String> getShipLocation() {
    return shipLocation;
  }

  public void setShipLocation(List<String> shipLocation) {
    this.shipLocation = shipLocation;
  }

  public Map<String, Object> createShipDTO() {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("shipType", this.getShipType());
    dto.put("shipLocation", this.getShipLocation());
    return dto;
  }

  public long getShipLength(Ship ship){
    long length = 0;
    switch (ship.getShipType()){
      case "carrier":
        length = 5;
        break ;
      case "battleship" :
        length = 4;
        break;
      case "submarine":
      case "destroyer":
        length = 3;
        break;
      case "patrol_boat":
        length = 2;
        break;
    }
    return length;
  }

}