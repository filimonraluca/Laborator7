package gameImpl;

import gameImpl.gameStrategy.StrategyType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clasa game reprezinta jocul propriu zis care are obiectul de tip Board reprezentand Tabla,
 * O list de obiecte de tip Player reprezentand toti jucatorii jocului,
 * o lista cu obiecte de tip Token reprezentand totalitate cartilor din joc,
 * o lista de threduri continand toate thredurile jocatorilor,
 * si un obiect de tipul ScoreManager responsabil cu deciderea scorului
 */
public class Game implements Runnable {
    Board board;
    List<Player> players;
    List<Token> tokens;
    List<Thread> playersThread;
    ScoreManager scoreManager;

    public Game(int playerNumber, int tokenNumber, int maxTokenValue, int k,  StrategyType strategyType) {
        initTokens(tokenNumber, maxTokenValue);
        board = new Board(tokens);
        players = new ArrayList<>();
        playersThread = new ArrayList<>();
        scoreManager = new ScoreManager(k);
        for (int i = 0; i < playerNumber; ++i) {
            players.add(new Player(String.format("gameImpl.Player %d", i + 1), board, scoreManager, strategyType));
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
    /** Metoda startPlayerThreads creaza cate un thred pentru fiecare jucator si il adauga in lista playersThread
     */
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
     * Metoda run() creeaza thredurile pentru fiecare jucator in parte si cat timp nu exista un castigator si tabla nu este
     * goala jucatorii primesc rand pe rand posibilitatea de a mai extrage un token prin intermediul obiectului communicator
     * al fiecarui jucator si metoda notify() care va trezi thredul. Dupa ce jucatorul isi va termina executia thredul corespunzator
     * acelui jucator va fi din nou suspendat prin metoda wait().
     * Metoda awakeAll() este folosita pentru a "anunta" toti jucatorii ca exista un castigator si jucal s-a terminat
     * Metoda joinAll() este folosita pentru a se asigura faptul ca executia unui thread s-a terminat inainte de a incepe executia
     * urmatorului
     * Metoda decideWinner() este folosita pentru a decide castigatorul
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

    /**
     * Metoda decideWinner() este folosita pentru a decide castigatorul meciului.
     * metoda decideWinnerOnEmptyBoard decide castigatorul in cazul in care tabla este goala si
     * nu exista niciun jucator care sa fi facut o progresie aritmetica de lungime k.
     * In cazul in care exista un castigator acesta este afisat pe ecran.
     */
    private void decideWinner() {
        scoreManager.decideWinnerOnEmptyBoard( players );

        if ( scoreManager.getWinner() != null ) {
            System.out.printf("The winner is %s\n", scoreManager.getWinner().getName());
        } else {
            System.out.println("Game was interrupted.");
        }
    }

    /**
     * Metoda joinAll() permite thredurilor jucatorilor sa isi astepte executia pana cand un alt thread si-a terminat-o.
     */
    private void joinAll() {
        for (Thread t : playersThread) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Trezeste pe rand toate thredurile jucatorilor.
     */
    private void awakeAll() {
        for (Player player:players) {
            Object communicator = player.getTurnCommunicator();
            synchronized (communicator) {
                communicator.notify();
            }
        }
    }
}
