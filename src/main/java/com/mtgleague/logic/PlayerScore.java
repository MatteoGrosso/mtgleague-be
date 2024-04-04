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

    private int gameWin;
    private int gameWinWithoutBye;
    private int gamePlayed;
    private int gamePlayedWithoutBye;
    private int matchWin;
    private int matchWinWithoutBye;
    private int matchDraw;
    private int matchPlayed;
    private int matchPlayedWithoutBye;

    private Set<Long> opponentsIds;

    public int getScore(){
        return matchWin * 3 + matchDraw;
    }

    public int getMatchWinRate(){
        int matchWinRate = matchWin/matchPlayed*100;
        return matchWinRate < 33 ? 33 : matchWinRate;
    }

    public int getFixedMatchWinRateWithoutBye(){
        return matchWinWithoutBye/matchPlayedWithoutBye*100;
    }

    public int getGameWinRateWithoutBye(){
        return gameWinWithoutBye/gamePlayedWithoutBye*100;
    }

    public int getFixedGameWinRateWithoutBye(){
        int gameWinRate = gameWinWithoutBye/gamePlayedWithoutBye*100;
        return gameWinRate < 33 ? 33 : gameWinRate;
    }

}
