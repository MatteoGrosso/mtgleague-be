package com.mtgleague.logic;

import lombok.*;

import java.util.Set;

//Business object used in the pairing logic
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PlayerScore {

    private Long id;
    private String name;
    private String surname;

    private int gameWin;
    private int gameWinWithoutBye;
    private int gamePlayed;
    private int gamePlayedWithoutBye;
    private int matchWin;
    private int matchWinWithoutBye;
    private int matchDraw;
    private int matchPlayed;
    private int matchPlayedWithoutBye;
    private double omw;
    private double gw;
    private double ogw;

    private Set<Long> opponentsIds;

    private int eventsPlayed; //used only in the players service for the rankList

    public int getScore(){
        return matchWin * 3 + matchDraw;
    }

    public double getOmw(){
        return Double.parseDouble(String.format("%.2f", omw));
    }
    public double getGw(){
        return Double.parseDouble(String.format("%.2f", gw));
    }
    public double getOgw(){
        return Double.parseDouble(String.format("%.2f", ogw));
    }

    public double getMatchWinRate(){
        double matchWinRate= ((double)matchWin/(double)matchPlayed)*100;
        return Double.parseDouble(String.format("%.2f", matchWinRate));
    }

    public double getFixedMatchWinRateWithoutBye(){
        double matchWinRate = matchPlayedWithoutBye > 0 ? matchWinWithoutBye/matchPlayedWithoutBye*100 : 0;
        return matchWinRate < 33 ? 33 : Double.parseDouble(String.format("%.2f", matchWinRate));
    }

    public double getGameWinRateWithoutBye(){
        double gameWinRate = gamePlayedWithoutBye > 0 ? gameWinWithoutBye/gamePlayedWithoutBye*100 : 0;
        return gameWinRate;
    }

    public double getFixedGameWinRateWithoutBye(){
        double gameWinRate = gamePlayedWithoutBye>0 ? gameWinWithoutBye/gamePlayedWithoutBye*100 : 0;
        return gameWinRate < 33 ? 33 : Double.parseDouble(String.format("%.2f", gameWinRate));
    }
}
