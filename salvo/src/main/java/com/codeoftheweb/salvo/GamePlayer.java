package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;

@Entity
public class GamePlayer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long ID;
  private Date joinDate;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="gameID")
  private Game game;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="playerID")
  private Player player;

  @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  Set<Ship> ship = new HashSet<>();

  @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  Set<Salvo> salvo = new HashSet<>();

  public GamePlayer() {
  }

  public GamePlayer(Player player, Game game, Date joinDate) {
    this.player = player;
    this.game = game;
    this.joinDate = joinDate;
  }

  public Set<Salvo> getSalvo() {
    return salvo;
  }

  public Set<Ship> getShip() {
    return ship;
  }

  public Date getJoinDate() {
    return joinDate;
  }

  public long getGamePlayerID() {
    return ID;
  }

  public long getGameID() {
    return ID;
  }

  @JsonIgnore
  public Player getPlayer() {
    return player;
  }

  @JsonIgnore
  public Game getGame() {
    return game;
  }

  public Map<String, Object> createGamePlayerDTO() {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("ID", this.getPlayer().getPlayerID());
    dto.put("user", this.getPlayer().getUserName());
    dto.put("gamePlayerID", this.getGamePlayerID());
    return dto;
  }

  public Map<String, Object> gameViewDTO () {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("ID", this.getGameID());
    dto.put("created", this.getGame().getCreationDate());
    dto.put("player", this.player.createPlayerDTO());
    dto.put("ships", this.getShip().stream().map(Ship::createShipDTO));
    dto.put("salvos", this.salvo.stream().map(Salvo::createSalvoDTO));
    return dto;
  }
}