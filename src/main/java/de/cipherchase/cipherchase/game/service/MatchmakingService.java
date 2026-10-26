package de.cipherchase.cipherchase.game.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import de.cipherchase.cipherchase.game.GameSession;
import de.cipherchase.cipherchase.persistence.GameRecords;
import de.cipherchase.cipherchase.persistence.GameRecordsRepository;
import de.cipherchase.cipherchase.persistence.User;
import de.cipherchase.cipherchase.persistence.UserRepository;



@Service 
public class MatchmakingService {

    private ArrayList<User> queue = new ArrayList<>();
    private ArrayList<GameSession> activeGameSessions = new ArrayList<>();
    private final GameRecordsRepository gameRecordsRepository;
    private final UserRepository userRepository;
    

    public MatchmakingService(GameRecordsRepository gameRecordRepository, UserRepository userRepository) {
        this.gameRecordsRepository = gameRecordRepository;
        this.userRepository = userRepository;
    }



    public synchronized GameSession joinQueue(User user) {
        queue.add(user);
        if (queue.size() >= 2) {
            //2x Index 0 notwendig weil beim Aufruf von remove(0) der Eintrag von remove(1) an die Stelle 0 rutscht
            GameSession gameSession = new GameSession(queue.remove(0), queue.remove(0), this);
            activeGameSessions.add(gameSession);
            return gameSession;
        }
        return null;
    }


    public void notifyFinishedGame(GameSession finishedGame) {
        saveGameResults(finishedGame);
        activeGameSessions.remove(finishedGame);
               
    }

    private void saveGameResults(GameSession gameSession) {
        //safes the stats for each player
        User winner = userRepository.findById(gameSession.getWinner().getPlayerID()).get();
        winner.incrementWonGames();
        winner.incrementPlayedGames();
        User looser = userRepository.findById(gameSession.getLooser().getPlayerID()).get();
        looser.incrementPlayedGames();
        //notwendig weil ohne save die Daten nur im Java-Objekt gespeichert werden
        userRepository.save(winner);    
        userRepository.save(looser);
        
        //saves the games data itself
        gameRecordsRepository.save(new GameRecords(winner, looser));
    }


    public synchronized void leaveQueue(User user) {
        queue.remove(user);
    }
}
