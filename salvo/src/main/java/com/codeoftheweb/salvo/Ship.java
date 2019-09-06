package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity

public class Ship {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long id;

  private String shipType;

  @Transient
  private List<String> possibleTypes = new LinkedList<String>(Arrays.asList("Destroyer", "Cruiser", "Submarine", "Battleship"));

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="gamePlayer_id")
  private GamePlayer gamePlayer;

  @ElementCollection
  @Column(name="location")
  private Set<String> locations;

  public Ship() { }

  public Ship(GamePlayer gamePlayer, String shipType, Set<String> locations) throws Exception {
    if (this.possibleTypes.contains(shipType)) {
      this.gamePlayer = gamePlayer;
      this.shipType = shipType;
      this.locations = locations;
    }
    else {
      throw new Exception("No existe ese tipo de barco");
    }
  }

  public GamePlayer getGamePlayer() {
    return gamePlayer;
  }

  public String getShipType() {
    return shipType;
  }

  public Set<String> getLocations() {
    return locations;
  }

}