package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity

public class GamePlayer {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long id;
  private Date joinDate;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="player_id")
  private Player player;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name="game_id")
  private Game game;

  @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  Set<Ship> ships = new HashSet<>();

  @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  Set<Salvo> salvos = new HashSet<>();

  public GamePlayer() {
  }

  public GamePlayer(Player player, Game game, Date joinDate) {
    this.player = player;
    this.game = game;
    this.joinDate = joinDate;
  }

  public Set<Salvo> getSalvos() {
    return salvos;
  }

  public Set<Ship> getShips() {
    return ships;
  }

  public Date getJoinDate() {
    return joinDate;
  }

  public long getGamePlayerId() {
    return id;
  }

  public long getGameId() {
    return id;
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
    dto.put("gamePlayerId", this.getGamePlayerId());
    dto.put("id", this.getPlayer().getPlayerId());
    dto.put("user", this.getPlayer().getUserName());
    return dto;
  }

  public Map<String, Object> gameViewDTO (){
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", this.getGameId());
    dto.put("created", this.getGame().getGameDate());
    dto.put("player", this.game.getGamePlayers());
    dto.put("ships", this.getShips().stream().map(ship -> ship.createShipDTO()));
    dto.put("salvos", this.game.getGamePlayers().stream().flatMap(gamePlayer -> gamePlayer.getSalvos().stream().map(salvo -> salvo.createSalvoDTO())));
    return dto;
  }
}