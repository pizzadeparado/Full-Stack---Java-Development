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
  Set<Salvo> salvoes = new HashSet<>();

  @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  Set<Ship> ships = new HashSet<>();

  public GamePlayer() {
  }

  public GamePlayer(Player player, Game game, Date joinDate) {
    this.player = player;
    this.game = game;
    this.joinDate = joinDate;
  }

  public Set<Salvo> getSalvoes() {
    return salvoes;
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

  public void addShip(Ship ship) {
    ships.add(ship);
  }

  public Map<String, Object> createGameDTO_GamePlayer() {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("gamePlayerId", this.getGamePlayerId());
    dto.put("player", this.getPlayer().createGameDTO_Player());
    dto.put("ships", this.getShips().stream().map(Ship::createGameDTO_Ship));
    return dto;
  }

  public Map<String, Object> dto_gameView (){
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", this.getGameId());
    dto.put("created", this.getGame().getGameDate());
    dto.put("gamePlayers", this.game.getGamePlayers().stream().map(GamePlayer::createGameDTO_GamePlayer));
    dto.put("ships", this.getShips().stream().map(Ship::createGameDTO_Ship));
    dto.put("salvoes", this.game.getGamePlayers().stream().flatMap(gp -> gp.getSalvoes().stream().map(Salvo::createGameDTO_Salvo)).collect(Collectors.toList()));
    return dto;
  }
}