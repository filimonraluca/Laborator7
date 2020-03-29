package gameImpl;

public class Token implements Comparable{
    int number;
    boolean joker;

    public Token(int number) {
        this.number = number;
    }

    public Token(boolean isJoker){
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

    @Override
    public int compareTo(Object o) {
        Token token = (Token) o;
        return number - token.getNumber();
    }
}
