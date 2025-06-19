package org.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("players")
public class Player {

    @Id
    private int id;
    private String name;  // 100 char
    private int gamesWon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

}