package com.demo;

import com.demo.domain.Fixture;
import com.demo.domain.Standing;
import com.demo.enums.WT20SuperSixTeam;
import com.demo.exception.NoPredictionsPossibleException;
import com.demo.exception.PredictionTooVagueException;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Prashanthan Subramaniyam
 */
public class Prediction {

    static final int TOTAL_FIXTURES = 5;
    Map<WT20SuperSixTeam, Standing> map;
    Map<WT20SuperSixTeam, Double> sortedRunRate;

    public static void main(String[] args) throws ParseException, NoPredictionsPossibleException, PredictionTooVagueException {

        WT20SuperSixTeam toToSimulateQualification = WT20SuperSixTeam.AFG;

        Standing[] pointsTable = new Standing[6];
        pointsTable[0] = new Standing(WT20SuperSixTeam.AFG, 5, 3, 2, 4, 880, 100, 810, 100, 0.700);
        pointsTable[1] = new Standing(WT20SuperSixTeam.SL, 4, 3, 1, 2, 771, 80, 735, 79, 0.384);
        pointsTable[2] = new Standing(WT20SuperSixTeam.IND, 4, 3, 2, 2, 720, 80, 730, 80, -0.125);
        pointsTable[3] = new Standing(WT20SuperSixTeam.PAK, 4, 3, 2, 2, 700, 80, 725, 80, -0.3125);
        pointsTable[4] = new Standing(WT20SuperSixTeam.AUS, 4, 3, 2, 2, 695, 80, 750, 80, -0.688);
        pointsTable[5] = new Standing(WT20SuperSixTeam.ENG, 5, 5, 0, 0, 800, 100, 1000, 100, -4.5);

        String defaultPattern = "dd-mm-yyyy";
        DateFormat formatter = new SimpleDateFormat(defaultPattern);
        Date date1 = formatter.parse("21-10-2022");
        Date date2 = formatter.parse("22-10-2022");

        Fixture[] fixture = new Fixture[2];
        fixture[0] = new Fixture(WT20SuperSixTeam.AUS, WT20SuperSixTeam.IND, date1);
        fixture[1] = new Fixture(WT20SuperSixTeam.PAK, WT20SuperSixTeam.SL, date2);

        Prediction prediction = new Prediction();
        prediction.simulateQualificationScenarios(toToSimulateQualification, pointsTable, fixture);

    }

    /**
     * @param toToSimulateQualification - Team for which the prediction is done
     * @param pointsTable               - Array of Standings not guranteed to be in any Order, the order is determined by the Standing#rank property
     * @param fixtures                  - Array of Fixture which shows the remaining mataches to be played.
     */
    public void simulateQualificationScenarios(WT20SuperSixTeam toToSimulateQualification, Standing[] pointsTable, Fixture[] fixtures) throws NoPredictionsPossibleException, PredictionTooVagueException {

        if (toToSimulateQualification != null && pointsTable.length != 0 && fixtures.length != 0) {

            // If there are more than 3 fixtures available a team
            if (fixtures.length > 3) {
                throw new PredictionTooVagueException("PredictionTooVague Error");
            }
            // if all the teams have already played all their fixtures
            if (isPlayedAllFixtures(pointsTable)) {
                throw new NoPredictionsPossibleException("NoPredictionsPossible errors");
            }

            //convert array into map
            arrayToMap(pointsTable);

            // if all the fixtures are lost
            if (map.get(toToSimulateQualification).getMatchesWon() == 0) {
                throw new NoPredictionsPossibleException("NoPredictionsPossible errors");
            }
            // if all the fixtures are won
            if (map.get(toToSimulateQualification).getMatchesWon() == TOTAL_FIXTURES) {
                System.out.println("AlreadyQualified");
            }


            for (int i = 0; i < fixtures.length; i++) {

                //skip if it's a same prediction team fixture
                if (toToSimulateQualification.equals(fixtures[i].getTeam1()) || toToSimulateQualification.equals(fixtures[i].getTeam2())) {
                    continue;
                }

                Map<WT20SuperSixTeam, Double> tmpMap = new HashMap<>();
                tmpMap.put(map.get(fixtures[i].getTeam1()).getTeam(), map.get(fixtures[i].getTeam1()).getNetRunRate());
                tmpMap.put(map.get(fixtures[i].getTeam2()).getTeam(), map.get(fixtures[i].getTeam2()).getNetRunRate());
                WT20SuperSixTeam[] sortedTeamByNTT = sortByNetRunRate(tmpMap);
                System.out.println("Fixture teams order by NRR, ascending  : " + Arrays.asList(sortedTeamByNTT));

                //Best Case
                System.out.println("Best Case :");
                double bestCasePredictedNRR = findWinningTeamNrRR(sortedTeamByNTT[0]);

                // if all matches are playing and check the qualification
                if (map.get(toToSimulateQualification).getMatchesPlayed() != TOTAL_FIXTURES) {
                    findToQualify(toToSimulateQualification, bestCasePredictedNRR, fixtures);
                } else {
                    System.out.println("To Qualify : Given team played all the fixtures");
                }

                System.out.println("----------------------------------------------");
                System.out.println();
                System.out.println("----------------------------------------------");

                //Worse Case
                System.out.println("Worst Case :");
                double worstCasePredictedNRR = findWinningTeamNrRR(sortedTeamByNTT[1]);

                // if all matches are playing and check the qualification
                if (map.get(toToSimulateQualification).getMatchesPlayed() != TOTAL_FIXTURES) {
                    findToQualify(toToSimulateQualification, worstCasePredictedNRR, fixtures);
                } else {
                    System.out.println("To Qualify : Given team played all the fixtures");
                }

                System.out.println("***********************************************");

            }

        } else if (fixtures.length == 0) {
            // if the fixtures array is empty
            throw new NoPredictionsPossibleException("NoPredictionsPossible errors");
        }

    }


    /**
     * convert Standing array into map
     *
     * @param pointsTable
     * @return
     */
    public Map<WT20SuperSixTeam, Standing> arrayToMap(Standing[] pointsTable) {

        map = new HashMap<>();
        for (int i = 0; i < pointsTable.length; i++) {
            map.put(pointsTable[i].getTeam(), pointsTable[i]);
        }
        return map;
    }

    /**
     * Find all the teams have already played all their fixtures
     *
     * @param pointsTable
     * @return
     */
    public boolean isPlayedAllFixtures(Standing[] pointsTable) {

        for (int i = 0; i < pointsTable.length; i++) {
            if (pointsTable[i].getMatchesPlayed() != TOTAL_FIXTURES) {
                return false;
            }
        }
        return true;
    }

    /**
     * Find the winning team's NRR
     *
     * @param
     */
    public double findWinningTeamNrRR(WT20SuperSixTeam team) {
        double predictedNRR = 0.0;

        System.out.println("Who Should Win : " + team);
        //when batting
        int averagesRunTeam1 = map.get(team).getTotalRunsScored() / map.get(team).getMatchesPlayed();
        System.out.println("Averages RunsScored when Batting : " + averagesRunTeam1);
        int predictedRunsScoredTeam = map.get(team).getTotalRunsScored() + averagesRunTeam1;
        System.out.println("Predicted RunsScored : " + predictedRunsScoredTeam);
        int predictedOversFacedTeam = map.get(team).getTotalOversFaced() + 20;
        System.out.println("Predicted OversFaced : " + predictedOversFacedTeam);

        //when bowling
        int averageRunsConceded = averagesRunTeam1 - getDeductRuns(team);
        System.out.println("Averages RunsScored when bowling : " + averageRunsConceded);
        int predictedRunsConceded = map.get(team).getTotalRunsConceded() + averageRunsConceded;
        System.out.println("Predicted RunsConceded : " + predictedRunsConceded);
        int predictedOversBowled = map.get(team).getTotalOversBowled() + 20;
        System.out.println("Predicted OversBowled : " + predictedOversBowled);

        //net run rate
        predictedNRR = getNetRunRate(predictedRunsScoredTeam, predictedOversFacedTeam, predictedRunsConceded, predictedOversBowled);
        System.out.println("Predicted NRR Would be : " + predictedNRR);

        return predictedNRR;
    }

    /**
     * To Qualify
     *
     * @param team
     * @param predictedNRR
     * @param fixtures
     */
    public void findToQualify(WT20SuperSixTeam team, double predictedNRR, Fixture[] fixtures) {
        System.out.println("To Qualify :");
        //when batting first
        int averageRun = map.get(team).getTotalRunsScored() / map.get(team).getMatchesPlayed();
        System.out.println("When batting first :");
        System.out.println("Team " + team + " will score : " + averageRun);
        int marginRun = findMargin(team, averageRun, predictedNRR);
        System.out.println("Margin of at least runs : " + marginRun + " in their 20 overs");

        //when bowling first
        WT20SuperSixTeam opponentTeam = null;
        // get the opponent team
        for (Fixture fixture : fixtures) {
            if (team.equals(fixture.getTeam1()) || team.equals(fixture.getTeam2())) {
                if (team.equals(fixture.getTeam1())) {
                    opponentTeam = fixture.getTeam2();
                } else {
                    opponentTeam = fixture.getTeam1();
                }
                break;
            }
        }
        int averageBattingScore = map.get(opponentTeam).getTotalRunsScored() / map.get(opponentTeam).getMatchesPlayed();
        System.out.println("When bowling first :");
        System.out.println("The opponent they are facing is " + opponentTeam + " who have an average batting score of : " + averageBattingScore);
        int scoreInOvers = findScoreInOvers(team, averageBattingScore, predictedNRR);
        System.out.println("Need to score the entire runs in : " + scoreInOvers + " overs");
    }

    /**
     * Find the margin runs needed to be won
     *
     * @param team
     * @param averageRun
     * @param predictedNRR
     * @return
     */
    public int findMargin(WT20SuperSixTeam team, int averageRun, double predictedNRR) {

        int totalRunsScored = map.get(team).getTotalRunsScored() + averageRun;
        int totalOversFaced = map.get(team).getTotalOversFaced() + 20;
        int totalRunsConceded = map.get(team).getTotalRunsConceded();
        int totalOversBowled = map.get(team).getTotalOversBowled() + 20;

        double averageRunsConceded = (((totalRunsScored / (double) totalOversFaced) - predictedNRR) * totalOversBowled) - totalRunsConceded;
        return (averageRun - (int) averageRunsConceded);
    }

    /**
     * Find the no of overs needed to finish the target score
     *
     * @param team
     * @param averageRun
     * @param predictedNRR
     * @return
     */
    public int findScoreInOvers(WT20SuperSixTeam team, int averageRun, double predictedNRR) {

        int totalRunsScored = map.get(team).getTotalRunsScored() + averageRun + 1;
        int totalOversFaced = map.get(team).getTotalOversFaced();
        int totalRunsConceded = map.get(team).getTotalRunsConceded() + averageRun;
        int totalOversBowled = map.get(team).getTotalOversBowled() + 20;

        //System.out.println("totalRunsScored 1 : " + totalRunsScored + " , totalOversFaced 1 : " + totalOversFaced + ", totalRunsConceded 1 : " + totalRunsConceded + ", totalOversBowled 1 : " + totalOversBowled);
        double averageOversFaced = (totalRunsScored / (double) (predictedNRR + (totalRunsConceded / (double) totalOversBowled))) - totalOversFaced;
        return ((int) averageOversFaced);
    }

    /**
     * sort by net run rate descending order
     *
     * @return
     */
    public void sortByNetRunRate() {
        Map<WT20SuperSixTeam, Double> unsortedMap = new HashMap<>();

        for (Map.Entry<WT20SuperSixTeam, Standing> set : map.entrySet()) {
            //skip non-qualified teams
            if (set.getValue().getPoints() != 0) {
                unsortedMap.put(set.getKey(), set.getValue().getNetRunRate());
            }
        }

        sortedRunRate = new LinkedHashMap<>();
        unsortedMap.entrySet().stream()
                .sorted(Map.Entry.<WT20SuperSixTeam, Double>comparingByValue().reversed())
                .forEachOrdered(x -> sortedRunRate.put(x.getKey(), x.getValue()));

    }

    /**
     * sort by net run rate Ascending order
     *
     * @return
     */
    public WT20SuperSixTeam[] sortByNetRunRate(Map<WT20SuperSixTeam, Double> unsortedMap) {
        Map<WT20SuperSixTeam, Double> sortedRunRate = new LinkedHashMap<>();
        unsortedMap.entrySet().stream()
                .sorted(Map.Entry.<WT20SuperSixTeam, Double>comparingByValue())
                .forEachOrdered(x -> sortedRunRate.put(x.getKey(), x.getValue()));

        // get the key set
        Set<WT20SuperSixTeam> keySet = sortedRunRate.keySet();

        return keySet.toArray(new WT20SuperSixTeam[keySet.size()]);
    }

    /**
     * deduction run for opposing team when calculation when batting first
     *
     * @param team
     * @return
     */
    public int getDeductRuns(WT20SuperSixTeam team) {
        // sort the Net run rate
        sortByNetRunRate();
        // get the key set
        Set<WT20SuperSixTeam> keySet = sortedRunRate.keySet();
        WT20SuperSixTeam[] keyArray = keySet.toArray(new WT20SuperSixTeam[keySet.size()]);
        //get the index of given team name
        int index = Arrays.asList(keyArray).indexOf(team);

        return (10 + (keyArray.length - index - 1) * 5);
    }

    /**
     * Net Run Rate Calculation
     *
     * @param totalRunsScored
     * @param totalOversFaced
     * @param totalRunsConceded
     * @param totalOversBowled
     * @return
     */
    public double getNetRunRate(int totalRunsScored, int totalOversFaced, int totalRunsConceded, int totalOversBowled) {

        //System.out.println("totalRunsScored : " + totalRunsScored + " , totalOversFaced : " + totalOversFaced + ", totalRunsConceded : " + totalRunsConceded + ", totalOversBowled : " + totalOversBowled);
        double nrr = totalRunsScored / (double) totalOversFaced - totalRunsConceded / (double) totalOversBowled;
        DecimalFormat decimalFormat = new DecimalFormat("###.###");
        System.out.println("Net run rate : " + decimalFormat.format(nrr));

        return Double.parseDouble(decimalFormat.format(nrr));
    }


}