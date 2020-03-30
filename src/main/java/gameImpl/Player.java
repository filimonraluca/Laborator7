package gameImpl;

import java.util.*;

/**
 * Clasa Player reprezinta jucatorul cu un nume, care joaca pentru o anumita table si are o lista tokens in mana
 * De asemenea, are si niste atribute precum arithmeticSize reprezentand lungimea progresiei aritmetice pe care ar trebui sa o aiba pentru a castiga
 * si numberOfJokers reprezentand numarul de jokeri pe care ii detine
 */
public class Player implements Runnable {
    String name;
    Board board;
    List<Token> tokens;
    int arithmeticSize;
    int numberOfJokers = 0;
    int score;


    public Player(String name, Board board, int k) {
        this.name = name;
        this.board = board;
        this.tokens = new ArrayList<>();
        this.arithmeticSize = k;
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
        int length = 0;
        while (!board.isEmpty() && !isDone) {
            tokens.add(board.extract(this));
            Token lastToken = tokens.get(tokens.size() - 1);
            if (lastToken.isJoker()) {
                numberOfJokers++;
            }
            length = maxLengthForProgression();
            if (length >= arithmeticSize) {
                isDone = true;
            }
        }
        if (isDone) {
            System.out.printf("I (%s) found a progression for the required length\n", name);
            board.claimWin(this);
        } else {
            System.out.printf("I (%s) did not find a progression for the required length, my max length is %d\n", name, length);
        }
    }

    /**
     * Aceasta metoda calculeaza care ar fi pasul cel mai mare pentru o progresie cu ajutorul metodei computeMaximumRatio()
     * De asemenea, incearca sa calculeze pentru fiecare pas incepand de la 1 lungimea maxima a unei progresii aritmetice ce se poate forma cu pasul respectiv
     * cu ajutorul metodei maxLengthForRatio(), retinand si lungimea maxima dupa calcularea tuturor lungimilor pentru fiecare pas in variabila maxLength
     * @return lungimea maxima dintre toate lungimile maxime calculate pentru o progresie pentru fiecare pas
     */
    public int maxLengthForProgression() {
        int maxLength = 0, length;
        int limit = computeMaximumRatio();
        for (int ratio = 1; ratio <= limit; ++ratio) {
            length = maxLengthForRatio(ratio);
            if (maxLength < length) {
                maxLength = length;
            }
        }
        return maxLength;
    }

    /**
     * Metoda maxLengthForRatio() parcurge lista de tokens a playerului si calculeaza incepand de la fiecare token separat, daca e diferit de joker,
     * care ar fi lungimea unei progresii daca se poate forma cu ajutorul metodei progressionLengthStartingFrom(),
     * iar la final retine lungimea maxima dintre toate progresiile calculate, token-ul de start si pasul prograsiei
     * @param ratio reprezinta pasul pentru care incearca sa se formeze progresia
     * @return returneaza lungimea maxima din toate progresiile formate
     */
    private int maxLengthForRatio(int ratio) {
        int maxLength = 0, length;
        Token maxTokenStart = null;
        for (Token t : tokens)
            if (!t.isJoker()) {
                length = progressionLengthStartingFrom(t, ratio);
                if (maxLength < length) {
                    maxLength = length;
                    maxTokenStart = t;
                }
            }
        System.out.printf("I (%s) found a progression of ratio %d and size %d starting from %d\n", name, ratio, maxLength, maxTokenStart.getNumber());
        return maxLength;
    }

    /**
     * Metoda computeMaximumRatio() calculeaza care ar fi pasul cel mai mare pentru o progresie formata din lista de tokens a jucatorului
     * Acesta este evident diferenta intre tokenul cu numarul cel mai mare si cel cu numarul cel mai mic pe care le-am aflat cu ajutorul metodelor max() si min()
     * si clasa Collections, pe care le-am putut aplica deoarece Tokens implementeaza interfata Comparable
     * @return returneaza pasul cel mai mare posibil pentru o progresie
     */
    private int computeMaximumRatio() {
        Token maximum = Collections.max(tokens);
        Token minimum = Collections.min(tokens);
        return maximum.getNumber() - minimum.getNumber();
    }

    /**
     * Metoda incearca sa calculeze lungimea unei progresii cu tokenurile jucatorului incepand de la un token de start si cu un anumit pas
     * Aceasta creaza un obiect nou de tip Token de fiecare data, current, care primeste valoarea tokenului precedent + pas-ul progresiei
     * Daca gasim acest nou Token in lista de tokens a jucatorului inseamna ca progresia se poate forma in continuare si crestem lungimea acesteia
     * Daca nu gasim Token-ul respectiv in lista jucatorului verificam daca acesta mai are jokeri, iar in caz afirmativ vom folosi unul pentru a creste lungimea progresiei
     * si pentru a continua sa o formam
     * Daca nu se gaseste tokenul necesar in lista de tokens a jucatorului si acesta nici nu mai are jokeri, oprim creerea progresiei
     * si returnam lungimea gasita in momentul respectiv
     * La fiecare pas in care s-a mai gasit o carte de adaugat la progresie se updateaza previous pentru a nu intra in bucla infinita
     * @param start reprezinta obiectul de tip Token de la care se va incepe crearea progresiei
     * @param ratio reprezinta pasul cu care se va incearca sa se creeze progresia
     * @return
     */
    private int progressionLengthStartingFrom(Token start, int ratio) {
        int numberOfJokersLeft = numberOfJokers;
        int size = 1;
        Token previous = start, current;
        do {
            current = new Token(previous.getNumber() + ratio);
            if (hasToken(current)) {
                previous = current;
                ++size;
            } else if (numberOfJokersLeft > 0) {
                --numberOfJokersLeft;
                previous = current;
                ++size;
            } else {
                previous = null;
            }
        } while (previous != null);
        return size;
    }

    /**
     * Metoda hasToken() parcurge intreaga lista de tokens a jucatorului si verifica daca gasesc o carte cu acelasi numar ca token-ul dat ca si parametru
     * @param current reprezinta obiectul de tip Token pe care vrem sa il cautam
     * @return returneaza true daca aceasta a fost gasit si false altfel
     */
    private boolean hasToken(Token current) {
        for (Token t : tokens) {
            if (current.getNumber() == t.getNumber()) {
                return true;
            }
        }
        return false;
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
