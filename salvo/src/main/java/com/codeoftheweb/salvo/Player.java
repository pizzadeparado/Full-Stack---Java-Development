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
  private String userName;
  private String password;

  @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
  private Set<GamePlayer> gamePlayers;

  @OneToMany(mappedBy="player", fetch= FetchType.EAGER)
  Set<Score> scores = new HashSet();

  public Player() {
  }

  public Player(String user, password) {
    this.userName = user;
    this.password = password;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUserName() {
    return userName;
  }

  public long getPlayerId() {
    return id;
  }

/*
  public String toString() {
    return userName;
  }

  public Set<GamePlayer> getGamePlayers() {
    return gamePlayers;
  }
*/

  @JsonIgnore
  public List<Game> getGame(){
    return gamePlayers.stream().map(sub -> sub.getGame()).collect(Collectors.toList());
  }

  public Map<String, Object> createPlayerDTO() {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("id", this.getPlayerId());
    dto.put("user", this.getUserName());
    return dto;
  }

  public Map<String, Object> createScoreDTO() {
    Map<String, Object> dto = new LinkedHashMap<>();
    dto.put("user", this.getUserName());
    dto.put("win", this.scores.stream().filter(score -> score.getScore()==1).count());
    dto.put("lose", this.scores.stream().filter(score -> score.getScore()==0).count());
    dto.put("tie", this.scores.stream().filter(score -> score.getScore()==0.5).count());
    return dto;
  }
}