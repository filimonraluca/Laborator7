package gameImpl;

import java.util.*;

/**
 * Clasa Player reprezinta jucatorul cu un nume, care joaca pentru o anumita table si are o lista tokens in mana
 * De asemenea, are si niste atribute precum arithmeticSize reprezentand lungimea progresiei aritmetice pe care ar trebui sa o aiba pentru a castiga
 * si numberOfJokers reprezentand numarul de jokeri pe care ii detine
 */
public class Player implements Runnable {
    final Object turnCommunicator;
    String name;
    Board board;
    List<Token> tokens;
    int numberOfJokers = 0;
    int score;
    ScoreManager scoreManager;


    public Player(String name, Board board, ScoreManager scoreManager) {
        this.name = name;
        this.board = board;
        this.tokens = new ArrayList<>();
        this.scoreManager = scoreManager;
        this.turnCommunicator = new Object();
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

    public boolean makeTurn(){
        tokens.add(board.extract(this));
        Token lastToken = tokens.get(tokens.size() - 1);

        if (lastToken.isJoker()) {
            numberOfJokers++;
        }

        return scoreManager.maxLengthForProgression(this) >= scoreManager.getArithmeticSize();
    }

    /**
     * Metoda run() este cea apelata in momentul in care se incepe executia unui thread pentru un player
     * Aceasta se incheie cand fie nu mai sunt carti pe tabla, caz in care castigatorul se decide in functie de progresia de cea mai lunga lungime,
     * fie un jucator a facut o progresie de lungimea ceruta caz in care aceasta este castigatorul si se apeleaza metoda claimWin() din clasa Board
     * In momentul in care s-a terminat executia metodei run(), s-a incheiat si firul de executie a threadului
     * Cat timp nu s-a incheiat jocul jucatorul extrage carti de pe tabla cu ajutorul metodei extract() din clasa Board
     * Se ia in cosiderare la fiecare carte trasa daa este sau nu joker si se calculeaza dupa fiecare tragere care este lungimea maxima
     * a unei progresii aritmetice cu ajutorul metodei maxLengthForProgression()
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
