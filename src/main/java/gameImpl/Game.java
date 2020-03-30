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

    public Game(int playerNumber, int tokenNumber, int maxTokenValue, int k) {
        initTokens(tokenNumber, maxTokenValue);
        board = new Board(tokens);
        players = new ArrayList<>();
        for (int i = 0; i < playerNumber; ++i) {
            players.add(new Player(String.format("gameImpl.Player %d", i + 1), board, k));
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

    /**
     * Metoda start() creeaza thredurile pentru fiecare jucator in parte si porneste executia fiecaruia prin metoda start() care
     * prin JVM va apela metoda run() a fiecarui thread
     * De asemenea, trecem prin toate threadurile create si apelam metoda join() care le v apermite threadurilor sa se astepte unu pe altul
     */
    public void start() {
        List<Thread> threads = new ArrayList<>();
        for (Player player : players) {
            Thread t = new Thread(player);
            t.start();
            threads.add(t);
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Player p : players)
            System.out.println(p);
    }
}
