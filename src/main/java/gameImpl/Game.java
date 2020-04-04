package gameImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clasa game reprezinta jocul propriu zis care are obiectul de tip Board reprezentand Tabla, O list de obiecte de tip Player reprezentand toti jucatorii jocului
 * si o lista cu obiecte de tip Token reprezentand totalitate cartilor din joc
 */
public class Game {
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
            players.add(new Player(String.format("gameImpl.Player %d", i + 1), board, scoreManager));
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

    /**
     * Metoda start() creeaza thredurile pentru fiecare jucator in parte si porneste executia fiecaruia prin metoda start() care
     * prin JVM va apela metoda run() a fiecarui thread
     * De asemenea, trecem prin toate threadurile create si apelam metoda join() pentru ca thredul jocului sa nu se termine pana
     * nu se termina thredurile jucatorilor.
     */
    public void start() {
        startPlayerThreads();
        int nextPlayer = 0;
        while ( !scoreManager.hasWinner() && !board.isEmpty() ) {
            Object communicator = players.get(nextPlayer).getTurnCommunicator();
            System.out.println(players.get(nextPlayer).getName());
            synchronized (communicator) {
                communicator.notify();
            }

            try {
                synchronized (communicator) {
                    communicator.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            nextPlayer = (nextPlayer + 1) % players.size();
        }
        awakeAll();
        joinAll();
        decideWinner();
    }

    private void decideWinner() {
        Player winner = scoreManager.getWinner();
        if (winner==null){
            int maxProgressionLen = -1;
            for (Player player : players) {
                int playersProgressionLen = scoreManager.maxLengthForProgression(player);
                if ( playersProgressionLen > maxProgressionLen ) {
                    maxProgressionLen = playersProgressionLen;
                    winner = player;
                }
            }
        }
        System.out.printf("The winner is %s", scoreManager.getWinner().getName());
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
