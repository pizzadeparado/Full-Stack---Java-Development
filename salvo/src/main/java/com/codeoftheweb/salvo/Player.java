package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity

public class Player {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
  @GenericGenerator(name = "native", strategy = "native")
  private long id;
  private String userName = "";

  @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
  private Set<GamePlayer> gamePlayers;

  @OneToMany(mappedBy="player", fetch= FetchType.EAGER)
  Set<Score> scores = new HashSet();

  public Player() {
  }

  public Player(String user) {
    this.userName = user;
  }

  public String getUserName() {
    return userName;
  }

  public long getPlayerId() {
    return id;
  }

  public String toString() {
    return userName;
  }

  public Set<GamePlayer> getGamePlayers() {
    return gamePlayers;
  }

  @JsonIgnore
  public List<Game> getGame(){
    return gamePlayers.stream().map(sub -> sub.getGame()).collect(Collectors.toList());
  }

  public Map<String, Object> createGameDTO_Player() {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", this.getPlayerId());
    dto.put("userName", this.getUserName());
    return dto;
  }

}