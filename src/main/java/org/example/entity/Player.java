package org.example.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "Players", indexes = {
        @Index(name = "index_player_name", columnList = "name", unique = true)
})
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false,unique = true, length =100)
    private String Name;

    public Player(int id, String name) {
        this.id = id;
        Name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
