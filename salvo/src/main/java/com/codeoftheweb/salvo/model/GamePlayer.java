package com.codeoftheweb.salvo.model;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class GamePlayer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long ID;
  private LocalDateTime joinDate;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="playerID")
  private Player player;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="gameID")
  private Game game;

  @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<Ship> ships = new HashSet<>();

  @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private Set<Salvo> salvos = new HashSet<>();

  public GamePlayer() {
  }

  public GamePlayer(Game game, Player player, LocalDateTime joinDate) {
    this.game = game;
    this.player = player;
    this.joinDate = joinDate;
  }

  public long getID() {
    return ID;
  }

  public void setID(long ID) {
    this.ID = ID;
  }

  public LocalDateTime getJoinDate() {
    return joinDate;
  }

  public void setJoinDate(LocalDateTime joinDate) {
    this.joinDate = joinDate;
  }

  public long getGameID() {
    return ID;
  }

  public Player getPlayer() {
    return player;
  }

  public void  setPlayer(Player player) {
    this.player = player;
  }

  public Game getGame() {
    return game;
  }

  public void  setGame(Game game) {
    this.game = game;
  }

  public long getGamePlayerID() {
    return ID;
  }

  public Set<Ship> getShips() {
    return ships;
  }

  public void addShip(Ship ship){
    this.ships.add(ship);
    ship.setGamePlayer(this);
  }

  public Set<Salvo> getSalvos() {
    return salvos;
  }

  public void addSalvo(Salvo salvo){
    this.salvos.add(salvo);
    salvo.setGamePlayer(this);
  }

  public GamePlayer getOpponent() {
    return this.getGame().getGamePlayer()
        .stream().filter(gamePlayer -> gamePlayer.getGamePlayerID() != this.getGamePlayerID())
        .findFirst()
        .orElse(null);
  }

  public Map<String, Object> createGamePlayerDTO() {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("playerID", this.getPlayer().getID());
    dto.put("gamePlayerID", this.getGamePlayerID());
    dto.put("user", this.getPlayer().getUserName());
    dto.put("hasShips", this.hasShips());
    return dto;
  }

  public Map<String, Object> gameViewDTO() {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("gameID", this.getGameID());
    dto.put("created", this.getGame().getCreationDate());
    dto.put("playerID", this.getPlayer().getID());
    dto.put("user", this.getPlayer().getUserName());
    dto.put("ships", this.getShips().stream().map(Ship::createShipDTO));
    dto.put("salvos", this.game.getGamePlayer().stream().flatMap(gamePlayer -> gamePlayer.getSalvos().stream().map(Salvo::createSalvoDTO)).collect(Collectors.toList()));
    return dto;
  }

  public String hasShips() {
    if (this.getShips().size() > 0) {
      return "Yes";
    } else {
      return "No";
    }
  }
}