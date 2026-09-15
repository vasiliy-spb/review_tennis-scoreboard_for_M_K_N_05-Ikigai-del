package org.example.model;

public class PlayerScore {
private int points = 0;
private int games = 0;
private int sets = 0;
private Integer tieBreakPoints = null;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public Integer getTieBreakPoints() {
        return tieBreakPoints;
    }

    public void setTieBreakPoints(Integer tieBreakPoints) {
        this.tieBreakPoints = tieBreakPoints;
    }
}
