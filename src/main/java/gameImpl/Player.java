package gameImpl;

import gameImpl.gameStrategy.Strategy;
import gameImpl.gameStrategy.StrategyCreator;
import gameImpl.gameStrategy.StrategyType;

import java.util.*;

/**
 * Clasa Player reprezinta jucatorul cu un nume, care joaca pentru o anumita table si are o lista tokens in mana
 * De asemenea, are si niste atribute precum un obiect de tipul scoreManager responsabil cu deciderea scorului,
 * un obiect de tipul Strategy care reprezinta strategia cu care va juca
 * si numberOfJokers reprezentand numarul de jokeri pe care ii detine
 */
public class Player implements Runnable {
    final Object turnCommunicator;
    private final Strategy strategy;
    String name;
    Board board;
    List<Token> tokens;
    int numberOfJokers = 0;
    ScoreManager scoreManager;


    public Player(String name, Board board, ScoreManager scoreManager, StrategyType strategyType) {
        this.name = name;
        this.board = board;
        this.tokens = new ArrayList<>();
        this.scoreManager = scoreManager;
        this.turnCommunicator = new Object();
        this.strategy = StrategyCreator.create( strategyType, board );
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public int getNumberOfJokers() {
        return numberOfJokers;
    }

    /**
     * Metoda hasToken() parcurge intreaga lista de tokens a jucatorului si verifica daca gasesc o carte cu acelasi numar ca token-ul dat ca si parametru
     * @param current reprezinta obiectul de tip Token pe care vrem sa il cautam
     * @return returneaza true daca aceasta a fost gasit si false altfel
     */
    public boolean hasToken(Token current) {
        for (Token t : tokens) {
            if (current.getNumber() == t.getNumber()) {
                return true;
            }
        }
        return false;
    }

    public Object getTurnCommunicator() {
        return turnCommunicator;
    }

    /**
     * In metoda makeTurn() jucatorul gaseste tokenul pe care doreste sa il extraga in functie de trategia pe care o aplica,
     * il extrage de pe tabla si il adauga in lista lui de tokenuri.
     * Se apeleaza metoda maxLenghtForProgression() care returneaza lungimea maxima a unui progresii cu tokenurile pe care
     * le-a extras jucatorul.
     * @return true daca jucatorul a facut o progresie arimetica mai mare sau egala cu lungimea progresiei necesare pentru
     * a castiga, false altfel
     */
    public boolean makeTurn(){
        Token token = strategy.findToken();
        if ( board.extract(token) ) {
            tokens.add(token);
            if (token.isJoker()) {
                numberOfJokers++;
            }
        }
        return scoreManager.maxLengthForProgression(this) >= scoreManager.getArithmeticSize();
    }

    /**
     * Metoda run() este cea apelata in momentul in care se incepe executia unui thread pentru un player
     * Cat timp tabla nu este goala si nu exista un castigator al meciului, jucatorul isi asteapta randul.
     * Obiectul turnCommunicator va asigura ca thredul jucatorului va fi blocat pana va primi notify() de la threadul
     * jocului. Dupa ce jucatorul si-a terminat tura trimite notify thredului jocului pentru ca acesta sa poata trece la jucatorul
     * urmator.
     */
    public void run() {
        boolean isDone = false;

        while (!board.isEmpty() && !isDone) {
            try {
                synchronized ( turnCommunicator) {
                    turnCommunicator.wait();
                }
            } catch (InterruptedException e) {
                System.out.printf("Player %s interrupted while waiting its turn\n", name);
                break;
            }

            if (!board.isEmpty() && !scoreManager.hasWinner()) {
                isDone = makeTurn();
            } else {
                isDone=true;
            }

            synchronized (turnCommunicator) {
                turnCommunicator.notify();
            }
        }

        if (isDone) {
            scoreManager.claimWin(this);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "gameImpl.Player{" +
                "name='" + name + '\'' +
                ", tokens=" + tokens +
                ", numberOfJokers=" + numberOfJokers +
                '}';
    }
}
