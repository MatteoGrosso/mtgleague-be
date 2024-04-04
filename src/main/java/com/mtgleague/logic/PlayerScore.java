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
    private int gamePlayed;
    private int matchWin;
    private int matchDraw;
    private int matchPlayed;

    private Set<Long> opponentsIds;

    public int getScore(){
        return matchWin * 3 + matchDraw;
    }

    public int getMatchWinRate(){
        int matchWinRate = matchWin/matchPlayed*100;
        return matchWinRate < 33 ? 33 : matchWinRate;
    }

    public int getFixedMatchWinRate(){
        return matchWin/matchPlayed*100;
    }

    public int getGameWinRate(){
        return gameWin/gamePlayed*100;
    }

    public int getFixedGameWinRate(){
        int gameWinRate = gameWin/gamePlayed*100;
        return gameWinRate < 33 ? 33 : gameWinRate;
    }

}
