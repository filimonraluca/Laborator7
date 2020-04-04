package gameImpl;

import java.util.Collections;

public class ScoreManager {
    int arithmeticSize;
    Player winner;
    final Object winnerLock = new Object();

    public ScoreManager(int arithmeticSize) {
        this.arithmeticSize = arithmeticSize;
    }

    /**
     * Am folosit un synchronized statement pentru a preveni intrarea a mai multor threaduri in verificare
     * Practic, ne ajuta sa alegem un singur castigator la final in momentul in care s-a gasit.
     * @param player reprezinta obiectul de tip Player care devine jucator daca nici un alt thread nu a intrat acolo inca
     */
    public void claimWin(Player player) {
        synchronized (winnerLock) {
            if (winner == null && maxLengthForProgression(player) >= arithmeticSize) {
                winner = player;
                System.out.printf("gameImpl.Player %s claimed it won\n", player.getName());
            } else if (winner != null) {
                System.out.printf("gameImpl.Player %s claimed it won after player %s\n", player.getName(), winner.getName());
            }
        }
    }

    public int getArithmeticSize() {
        return arithmeticSize;
    }

    public Player getWinner() {
        return winner;
    }

    /**
     * Aceasta metoda calculeaza care ar fi pasul cel mai mare pentru o progresie cu ajutorul metodei computeMaximumRatio()
     * De asemenea, incearca sa calculeze pentru fiecare pas incepand de la 1 lungimea maxima a unei progresii aritmetice ce se poate forma cu pasul respectiv
     * cu ajutorul metodei maxLengthForRatio(), retinand si lungimea maxima dupa calcularea tuturor lungimilor pentru fiecare pas in variabila maxLength
     * @return lungimea maxima dintre toate lungimile maxime calculate pentru o progresie pentru fiecare pas
     */
    public int maxLengthForProgression(Player player) {
        int maxLength = 0, length;
        int limit = computeMaximumRatio(player);
        for (int ratio = 1; ratio <= limit; ++ratio) {
            length = maxLengthForRatio(player, ratio);
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
     *
     * @param player
     * @param ratio reprezinta pasul pentru care incearca sa se formeze progresia
     * @return returneaza lungimea maxima din toate progresiile formate
     */
    private int maxLengthForRatio(Player player, int ratio) {
        int maxLength = 0, length;
        Token maxTokenStart = null;
        for (Token t : player.getTokens())
            if (!t.isJoker()) {
                length = progressionLengthStartingFrom(player, t, ratio);
                if (maxLength < length) {
                    maxLength = length;
                    maxTokenStart = t;
                }
            }
//        System.out.printf("I (%s) found a progression of ratio %d and size %d starting from %d\n", player.getName(), ratio, maxLength, maxTokenStart.getNumber());
        return maxLength;
    }

    /**
     * Metoda computeMaximumRatio() calculeaza care ar fi pasul cel mai mare pentru o progresie formata din lista de tokens a jucatorului
     * Acesta este evident diferenta intre tokenul cu numarul cel mai mare si cel cu numarul cel mai mic pe care le-am aflat cu ajutorul metodelor max() si min()
     * si clasa Collections, pe care le-am putut aplica deoarece Tokens implementeaza interfata Comparable
     * @return returneaza pasul cel mai mare posibil pentru o progresie
     * @param player
     */
    private int computeMaximumRatio(Player player) {
        if (player.getTokens().size()==0) {
            return 0;
        }
        Token maximum = Collections.max(player.getTokens());
        Token minimum = Collections.min(player.getTokens());
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
     *
     * @param player
     * @param start reprezinta obiectul de tip Token de la care se va incepe crearea progresiei
     * @param ratio reprezinta pasul cu care se va incearca sa se creeze progresia
     * @return
     */
    private int progressionLengthStartingFrom(Player player, Token start, int ratio) {
        int numberOfJokersLeft = player.getNumberOfJokers();
        int size = 1;
        Token previous = start, current;
        do {
            current = new Token(previous.getNumber() + ratio);
            if (player.hasToken(current)) {
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

    public boolean hasWinner() {
        synchronized (winnerLock) {
            return winner != null;
        }
    }
}
