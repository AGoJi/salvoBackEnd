package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;
    public Date date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    Set<Ship> ships = new HashSet<>();

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    Set<Salvo> salvoes = new HashSet<>();

    public GamePlayer(){}

    public GamePlayer(Game game, Player player) {
        this.game=game;
        this.player=player;
        this.date = new Date();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {return date;}

    public void setDate(Date date) {this.date = date;}

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) { this.player = player;}

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public void addShip(Ship ship) {
        this.ships.add(ship);
        ship.setGamePlayer(this);
    }

    public Long getId() {return id;}

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public void setSalvoes(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }

    public void addSalvo(Salvo salvo) {
        this.salvoes.add(salvo);
        salvo.setGamePlayer(this);
    }

    public Score getScore(){
        return this.player.giveMeScore(this.game);
    }

    @Override
    public String toString() {
        return "GamePlayer{" +
                "id=" + id +
                ", date=" + date +
                ", game=" + game +
                ", player=" + player +
                ", ships=" + ships +
                ", salvoes=" + salvoes +
                '}';
    }
}