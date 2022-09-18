package com.demo.domain;

import com.demo.enums.WT20SuperSixTeam;

public class Standing {
    WT20SuperSixTeam team;
    int matchesPlayed;
    int matchesLost;
    int matchesWon;
    int points;
    int totalRunsScored;
    int totalOversFaced;
    int totalRunsConceded;
    int totalOversBowled;
    double netRunRate;

    public Standing(WT20SuperSixTeam team, int matchesPlayed, int matchesLost, int matchesWon, int points, int totalRunsScored, int totalOversFaced, int totalRunsConceded, int totalOversBowled, double netRunRate) {
        this.team = team;
        this.matchesPlayed = matchesPlayed;
        this.matchesLost = matchesLost;
        this.matchesWon = matchesWon;
        this.points = points;
        this.totalRunsScored = totalRunsScored;
        this.totalOversFaced = totalOversFaced;
        this.totalRunsConceded = totalRunsConceded;
        this.totalOversBowled = totalOversBowled;
        this.netRunRate = netRunRate;
    }

    public WT20SuperSixTeam getTeam() {
        return team;
    }

    public void setTeam(WT20SuperSixTeam team) {
        this.team = team;
    }

    public int getMatchesPlayed() {
        return matchesPlayed;
    }

    public void setMatchesPlayed(int matchesPlayed) {
        this.matchesPlayed = matchesPlayed;
    }

    public int getMatchesLost() {
        return matchesLost;
    }

    public void setMatchesLost(int matchesLost) {
        this.matchesLost = matchesLost;
    }

    public int getMatchesWon() {
        return matchesWon;
    }

    public void setMatchesWon(int matchesWon) {
        this.matchesWon = matchesWon;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getTotalRunsScored() {
        return totalRunsScored;
    }

    public void setTotalRunsScored(int totalRunsScored) {
        this.totalRunsScored = totalRunsScored;
    }

    public int getTotalOversFaced() {
        return totalOversFaced;
    }

    public void setTotalOversFaced(int totalOversFaced) {
        this.totalOversFaced = totalOversFaced;
    }

    public int getTotalRunsConceded() {
        return totalRunsConceded;
    }

    public void setTotalRunsConceded(int totalRunsConceded) {
        this.totalRunsConceded = totalRunsConceded;
    }

    public int getTotalOversBowled() {
        return totalOversBowled;
    }

    public void setTotalOversBowled(int totalOversBowled) {
        this.totalOversBowled = totalOversBowled;
    }

    public double getNetRunRate() {
        return netRunRate;
    }

    public void setNetRunRate(double netRunRate) {
        this.netRunRate = netRunRate;
    }

    @Override
    public String toString() {
        return "{" +
                "team=" + team +
                ", matchesPlayed=" + matchesPlayed +
                ", matchesLost=" + matchesLost +
                ", matchesWon=" + matchesWon +
                ", points=" + points +
                ", totalRunsScored=" + totalRunsScored +
                ", totalOversFaced=" + totalOversFaced +
                ", totalRunsConceded=" + totalRunsConceded +
                ", totalOversBowled=" + totalOversBowled +
                ", netRunRate=" + netRunRate +
                '}';
    }
}
