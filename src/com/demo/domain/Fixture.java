package com.demo.domain;

import com.demo.enums.WT20SuperSixTeam;

import java.util.Date;

public class Fixture {

    WT20SuperSixTeam team1;
    WT20SuperSixTeam team2;
    Date fixtureDate;

    public Fixture(WT20SuperSixTeam team1, WT20SuperSixTeam team2, Date fixtureDate) {
        this.team1 = team1;
        this.team2 = team2;
        this.fixtureDate = fixtureDate;
    }

    public WT20SuperSixTeam getTeam1() {
        return team1;
    }

    public void setTeam1(WT20SuperSixTeam team1) {
        this.team1 = team1;
    }

    public WT20SuperSixTeam getTeam2() {
        return team2;
    }

    public void setTeam2(WT20SuperSixTeam team2) {
        this.team2 = team2;
    }

    public Date getFixtureDate() {
        return fixtureDate;
    }

    public void setFixtureDate(Date fixtureDate) {
        this.fixtureDate = fixtureDate;
    }

    @Override
    public String toString() {
        return "Fixture{" +
                "team1=" + team1 +
                ", team2=" + team2 +
                ", fixtureDate=" + fixtureDate +
                '}';
    }
}
