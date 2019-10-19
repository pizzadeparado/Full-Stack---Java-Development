package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long id;
  private Date gameDate;

  @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
  private Set<GamePlayer> gamePlayers;

  @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
  private Set<Score> scores = new HashSet<>();

  public Game() {
  }

  public Game(Date gameDate) {
    this.gameDate = new Date();
  }

  public Date getGameDate() {
    return gameDate;
  }

  public long getGameId() {
    return id;
  }

  @JsonIgnore
  public List<Player> getPlayer() {
    return gamePlayers.stream().map(sub -> sub.getPlayer()).collect(Collectors.toList());
  }

  public Set<GamePlayer> getGamePlayers() {
    return gamePlayers;
  }

  public Map<String, Object> createGameDTO() {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", this.getGameId());
    dto.put("created", this.getGameDate().getTime());
    dto.put("players", this.getGamePlayers().stream().map(gamePlayer -> gamePlayer.gameViewDTO()));
    return dto;
  }
}