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
  private long ID;
  private Date creationDate;

  @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
  private Set<GamePlayer> gamePlayer;

  @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
  private Set<Score> scores = new HashSet<>();

  public Game() {
  }

  public Game(Date creationDate) {
    this.creationDate = new Date();
  }

  public Date getCreationDate() {
    return creationDate;
  }

  public long getGameID() {
    return ID;
  }

  @JsonIgnore
  public List<Player> getPlayer() {
    return gamePlayer.stream().map(GamePlayer::getPlayer).collect(Collectors.toList());
  }

  public Set<GamePlayer> getGamePlayer() {
    return gamePlayer;
  }

  public Map<String, Object> createGameDTO() {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("gameID", this.getGameID());
    dto.put("created", this.getCreationDate().getTime());
    dto.put("player", this.getGamePlayer().stream().map(GamePlayer::createGamePlayerDTO));
    return dto;
  }
}