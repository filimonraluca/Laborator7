package gameImpl;

/**
 * clasa Token reprezinta cartile ce se vor trage de pe masa
 * Acestea au fie un numar, fie sunt goale caz in care le consideram jokeri si inlocuim numarul cu -1
 * De asemenea, implementeaza interfata Comporable pentru a impune o ordine a elementelor
 */
public class Token implements Comparable {
    int number;
    boolean joker;

    public Token(int number) {
        this.number = number;
    }

    public Token(boolean isJoker) {
        joker = isJoker;
        number = -1;
    }

    public int getNumber() {
        return number;
    }

    public boolean isJoker() {
        return joker;
    }

    @Override
    public String toString() {
        return "gameImpl.Token{" +
                "number=" + number +
                '}';
    }

    /**
     * Suprascriem functie compareTo pentru a impune ordonarea elementelor in functie de numarul cartii.
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        Token token = (Token) o;
        return number - token.getNumber();
    }
}
