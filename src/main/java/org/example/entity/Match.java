package org.example.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player1_id", nullable = false)
    private Player players1;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player2_id", nullable = false)
    private Player player2;

    @ManyToOne(optional = false)
    @JoinColumn(name = "winer_id", nullable = false)
    private Player winer;

    public Match(Player players1, Player player2, Player winer) {
        this.players1 = players1;
        this.player2 = player2;
        this.winer = winer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player getPlayers1() {
        return players1;
    }

    public void setPlayers1(Player players1) {
        this.players1 = players1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Player getWiner() {
        return winer;
    }

    public void setWiner(Player winer) {
        this.winer = winer;
    }
}
