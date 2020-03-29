public class Token implements Comparable{
    int number;
    boolean joker;

    public Token() {
    }

    public Token(int number) {
        this.number = number;
    }

    public Token(boolean isJoker){
        joker = isJoker;
        number = -1;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public boolean isJoker() {
        return joker;
    }

    @Override
    public String toString() {
        return "Token{" +
                "number=" + number +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        Token token = (Token) o;
        return number - token.getNumber();
    }
}
