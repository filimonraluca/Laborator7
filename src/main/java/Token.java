public class Token {
    int number;
    boolean joker;

    public Token() {
        this.number = (int) (Math.random()*100);
    }

    public Token(int number) {
        this.number = number;
    }

    public Token(boolean isJoker){
        joker = isJoker;
        number = -1;
    }

    @Override
    public String toString() {
        return "Token{" +
                "number=" + number +
                '}';
    }
}
