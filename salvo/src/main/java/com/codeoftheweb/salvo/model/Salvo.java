package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Salvo {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long ID;
  private int turn;

  @ElementCollection
  @Column(name = "salvoLocation")
  private List<String> salvoLocation = new ArrayList<>();

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "gamePlayerID")
  private GamePlayer gamePlayer;

  public Salvo() {

  }

  public Salvo(int turn, List<String> salvoLocation) {
    this.turn = turn;
    this.salvoLocation = salvoLocation;
  }

  public long getID() {
    return this.ID;
  }

  public GamePlayer getGamePlayer() {
    return gamePlayer;
  }

  public void setGamePlayer(GamePlayer gamePlayer) {
    this.gamePlayer = gamePlayer;
  }

  public long getTurn() {
    return this.turn;
  }

  public void setTurn(int turn) {
    this.turn = turn;
  }

  public List<String> getSalvoLocation() {
    return salvoLocation;
  }

  public void setSalvoLocation(List<String> salvoLocation) {
    this.salvoLocation = salvoLocation;
  }

  public List<String> getHits(List<String> myFires, Set<Ship> opponentShips) {
    List<String> opponentLocations = new ArrayList<>();
    opponentShips.forEach(ship -> opponentLocations.addAll(ship.getShipLocation()));

    return myFires.stream().filter(fire -> opponentLocations.stream().anyMatch(locations -> locations.equals(fire))).collect(Collectors.toList());
  }

  public List<Ship> getSunken(Set<Salvo> mySalvos, Set<Ship> opponentShips) {
    List<String> allFires = new ArrayList<>();
    mySalvos.forEach(salvo -> allFires.addAll(salvo.getSalvoLocation()));

    return opponentShips.stream().filter(ship -> allFires.containsAll(ship.getShipLocation())).collect(Collectors.toList());
  }

  public Map<String, Object> createSalvoDTO () {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("player", this.getGamePlayer().getPlayer().getID());
    dto.put("turn", this.getTurn());
    dto.put("location", this.getSalvoLocation());

    GamePlayer opponent = this.getGamePlayer().getOpponent();

    if(opponent != null) {
      Set<Ship> opponentShips = opponent.getShips();
      dto.put("hits", this.getHits(this.getSalvoLocation(), opponentShips));

      Set<Salvo> mySalvos = this.getGamePlayer().getSalvos().stream().filter(salvo -> salvo.getTurn() <= this.getTurn()).collect(Collectors.toSet());;
      dto.put("sunken", this.getSunken(mySalvos, opponentShips).stream().map(Ship::createShipDTO));
    }
    return dto;
  }
}
