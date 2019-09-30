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
  private List<String> possibleTypes = new LinkedList<String>(Arrays.asList("Destroyer", "Cruiser", "Submarine", "Battleship", "PatrolBoat"));

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="gamePlayer_id")
  private GamePlayer gamePlayer;

  @ElementCollection
  @Column(name="location")
  private List<String> locations;

  public Ship() { }

  public Ship(GamePlayer gamePlayer, String shipType, List<String> locations) throws Exception {
    if (this.possibleTypes.contains(shipType)) {
      this.gamePlayer = gamePlayer;
      this.shipType = shipType;
      this.locations = locations;
    }
    else {
      throw new Exception("There are no ships by this name.");
    }
  }

  public GamePlayer getGamePlayer() {
    return gamePlayer;
  }

  public String getShipType() {
    return shipType;
  }

  public List<String> getLocations() {
    return locations;
  }

  public Map<String, Object> createGameDTO_Ship () {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("shipType", this.getShipType());
    dto.put("locations", this.getLocations());
  }

}