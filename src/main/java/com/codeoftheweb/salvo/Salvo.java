package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    private Integer turn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name = "salvoLocation")
    private List<String> salvoLocation = new ArrayList<>();

    public Salvo() { }

    public Salvo(Integer turn, List<String> salvoLocation) {
        this.turn = turn;
        this.salvoLocation = salvoLocation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTurn() {
        return turn;
    }

    public void setTurn(Integer turn) {
        this.turn = turn;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getSalvoLocation() {
        return salvoLocation;
    }

    public void setSalvoLocation(List<String> salvoLocation) {
        this.salvoLocation = salvoLocation;
    }

    @Override
    public String toString() {
        return "Salvo{" +
                "id=" + id +
                ", turn=" + turn +
                ", gamePlayer=" + gamePlayer +
                ", salvoLocation=" + salvoLocation +
                '}';
    }
}