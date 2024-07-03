package com.mtgleague.logic;

import com.google.common.util.concurrent.AtomicDouble;
import com.mtgleague.model.Event;
import com.mtgleague.service.RoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class Pairing {
    private final RoundService roundService;

    // QuickSort algorithm implementation
    public void quickSort(List<PlayerScore> players, int low, int high) {
        if (low < high) {
            int pi = partition(players, low, high);
            quickSort(players, low, pi - 1);
            quickSort(players, pi + 1, high);
        }
    }

    /*
    this partition follows the rules magic the gathering use to do the pairings and the 3-step rules to break a tie:
        1- players points;
        2- omw (value that refers to the winRate (of the matches) of the opponents / the number of opponents)
        3- gw (value that refers to the winRate (of the single games) of the player)
        4- ogw (value that refers to the winRate (of the single games) of the opponents / the number of opponents)

    !!!Be careful: magic the gathering uses this rules "when calculating omw and ogw, the single winRates are set to 33 if they are below"!!!
     */
    private int partition(List<PlayerScore> players, int low, int high) {
        PlayerScore pivot = players.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            PlayerScore player = players.get(j);
            if (player.getScore() > pivot.getScore()) {
                i++;
                swap(players, i, j);
            }else if(player.getScore() == pivot.getScore()){
                double omwP= omwCalc(player, players);
                double omwPivot= omwCalc(pivot, players);
                if (omwP > omwPivot) {
                    i++;
                    swap(players, i, j);
                }else if(omwP == omwPivot){
                    double gwP= gwCalc(player);
                    double gwPivot= gwCalc(pivot);
                    if (gwP > gwPivot) {
                        i++;
                        swap(players, i, j);
                    }else if(gwP == gwPivot){
                        double ogwP= ogwCalc(player, players);
                        double ogwPivot= ogwCalc(pivot, players);
                        if (ogwP > ogwPivot) {
                            i++;
                            swap(players, i, j);
                        }
                    }
                }
            }
        }
        swap(players, i + 1, high);
        return i + 1;
    }

    private void swap(List<PlayerScore> players, int i, int j) {
        PlayerScore temp = players.get(i);
        players.set(i, players.get(j));
        players.set(j, temp);
    }

    //END QuickSort algorithm implementation

    //omw is the value that refers to the winRate (of the matches) of the opponents / the number of opponents
    //!!!Be careful: magic the gathering uses this rules "when calculating omw and ogw, the single winRates are set to 33 if they are below"!!!
    private double omwCalc(PlayerScore player, List<PlayerScore> players){
        AtomicDouble omwPlayer= new AtomicDouble();
        player.getOpponentsIds().forEach(
                opponentId -> {
                    Optional<PlayerScore> ps= players.stream().filter(
                            p -> p.getId() == opponentId
                    ).findFirst();
                    if(ps.isPresent()){
                        omwPlayer.addAndGet( ps.get().getFixedMatchWinRateWithoutBye());
                    }
                }
        );
        int numberOfOpponents= player.getOpponentsIds().size();
        double omw= numberOfOpponents>0 ? omwPlayer.get()/numberOfOpponents : 0;
        player.setOmw(omw);
        return omw;
    }

    //omw is the value that refers to the winRate (of the single games) of the player
    private double gwCalc(PlayerScore player){
        double gw= player.getGameWinRateWithoutBye();
        player.setGw(gw);
        return gw;
    }

    //omw is the value that refers to the winRate (of the single games) of the opponents / the number of opponents
    //!!!Be careful: magic the gathering uses this rules "when calculating omw and ogw, the single winRates are set to 33 if they are below"!!!
    private double ogwCalc(PlayerScore player, List<PlayerScore> players){
        AtomicDouble ogwPlayer= new AtomicDouble();
        player.getOpponentsIds().forEach(
                opponentId -> {
                    Optional<PlayerScore> ps= players.stream().filter(
                            p -> p.getId() == opponentId
                    ).findFirst();
                    if(ps.isPresent()){
                        ogwPlayer.addAndGet(ps.get().getFixedGameWinRateWithoutBye());
                    }
                }
        );
        int numberOfOpponents= player.getOpponentsIds().size();

        double ogw= numberOfOpponents>0 ? ogwPlayer.get()/numberOfOpponents : 0;
        player.setOmw(ogw);
        return ogw;
    }

    public void doPairings(List<PlayerScore> players, Event event, int currentTurn){
        Boolean byeIsNeeded= players.size()%2 == 1;

        List<PlayerScore> notYetPaired= players;

        if (!event.isStarted()) {
            for (int index = 0; index < players.size() - 1; index++) {
                PlayerScore player1= players.get(index);
                PlayerScore player2= players.get(++index);
                roundService.createRound(player1.getId(), player1.getName(), player1.getSurname(), player2.getId(), player2.getName(), player2.getSurname(),0,0, event, currentTurn, false);
            }
            if (byeIsNeeded){
                PlayerScore player1= players.get(players.size()-1);
                roundService.createRound(player1.getId(), player1.getName(), player1.getSurname(), null, null, null, 2, 0, event, currentTurn, true);
            }
        } else {
            if (byeIsNeeded){
                PlayerScore player= notYetPaired.get(notYetPaired.size()-1);
                roundService.createRound(player.getId(), player.getName(), player.getSurname(), null, null, null, 2, 0, event, currentTurn, true);
                notYetPaired.remove(player);
            }
            do {
                PlayerScore player1= notYetPaired.get(notYetPaired.size()-1);

                List<PlayerScore> possibleOpponents= notYetPaired.stream()
                        .filter(
                                p -> p.getId() != player1.getId() && !player1.getOpponentsIds().contains(p.getId()))
                        .collect(Collectors.toList());

                PlayerScore player2 = possibleOpponents.get(possibleOpponents.size() - 1);

                roundService.createRound(player1.getId(), player1.getName(), player1.getSurname(), player2.getId(), player2.getName(), player2.getSurname(),0,0, event, currentTurn,false);

                notYetPaired.remove(player1);
                notYetPaired.remove(player2);
            } while(notYetPaired.size() > 1);
        }

    }
}