package gameImpl;

import gameImpl.gameStrategy.StrategyType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clasa game reprezinta jocul propriu zis care are obiectul de tip Board reprezentand Tabla, O list de obiecte de tip Player reprezentand toti jucatorii jocului
 * si o lista cu obiecte de tip Token reprezentand totalitate cartilor din joc
 */
public class Game implements Runnable {
    Board board;
    List<Player> players;
    List<Token> tokens;
    List<Thread> playersThread;
    ScoreManager scoreManager;

    public Game(int playerNumber, int tokenNumber, int maxTokenValue, int k) {
        initTokens(tokenNumber, maxTokenValue);
        board = new Board(tokens);
        players = new ArrayList<>();
        playersThread = new ArrayList<>();
        scoreManager = new ScoreManager(k);
        for (int i = 0; i < playerNumber; ++i) {
            players.add(new Player(String.format("gameImpl.Player %d", i + 1), board, scoreManager, StrategyType.LAST_TOKEN));
        }
    }

    /**
     * Metoda initTokens() genereaza un numar random pentru cati jokeri vor fi in joc si
     * creeaza o lista de Integer si pe care o amesteca si alege de acolo primele (numarul total de tokens - numar jokeri) tokens
     * La final creeaza cate un obiect de tip Token pentru fiecare numar ales si cate un obiect de tip Token pentru fiecare joker (cu contructori diferiti),
     * pe care ii adauga in lista de tokeni finali si pe care o amestecam la final
     * @param tokenNumber   reprezinta numarul de tokens care terbuie alesi
     * @param maxTokenValue reprezinta valoarea maxima pe care o poate lua un token
     */
    public void initTokens(int tokenNumber, int maxTokenValue) {
        List<Integer> tokensValues = new ArrayList<>();
        for (Integer i = 0; i < maxTokenValue; i++)
            tokensValues.add(i);
        Collections.shuffle(tokensValues);

        int nrOfJokers = (int) (Math.random() * tokenNumber / 2);
        tokens = new ArrayList<Token>();
        for (int i = 0; i < tokenNumber - nrOfJokers; ++i) {
            tokens.add(new Token(tokensValues.get(i)));
        }
        for (int i = 0; i < nrOfJokers; ++i) {
            tokens.add(new Token(true));
        }
        Collections.shuffle(tokens);
    }

    public void startPlayerThreads() {
        for (Player player : players) {
            Thread t = new Thread(player);
            t.start();
            playersThread.add(t);
        }
    }

    public List<Thread> getPlayersThread() {
        return playersThread;
    }

    public ScoreManager getScoreManager() {
        return scoreManager;
    }

    /**
     * Metoda start() creeaza thredurile pentru fiecare jucator in parte si porneste executia fiecaruia prin metoda start() care
     * prin JVM va apela metoda run() a fiecarui thread
     * De asemenea, trecem prin toate threadurile create si apelam metoda join() pentru ca thredul jocului sa nu se termine pana
     * nu se termina thredurile jucatorilor.
     */
    public void run() {
        startPlayerThreads();
        int nextPlayer = 0;
        while ( !scoreManager.hasWinner() && !board.isEmpty() ) {
            Object communicator = players.get(nextPlayer).getTurnCommunicator();
            System.out.printf("Player %s\n", players.get(nextPlayer));
            synchronized (communicator) {
                communicator.notify();
            }

            try {
                synchronized (communicator) {
                    communicator.wait();
                }
            } catch (InterruptedException e) {
                System.out.println("Game interrupted while it was waiting for player to take its turn");
                break;
            }

            nextPlayer = (nextPlayer + 1) % players.size();
        }
        awakeAll();
        joinAll();
        decideWinner();
    }

    private void decideWinner() {
        scoreManager.decideWinnerOnEmptyBoard( players );

        if ( scoreManager.getWinner() != null ) {
            System.out.printf("The winner is %s\n", scoreManager.getWinner().getName());
        } else {
            System.out.println("Game was interrupted.");
        }
    }

    private void joinAll() {
        for (Thread t : playersThread) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void awakeAll() {
        for (Player player:players) {
            Object communicator = player.getTurnCommunicator();
            synchronized (communicator) {
                communicator.notify();
            }
        }
    }
}
