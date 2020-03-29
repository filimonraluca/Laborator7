import java.util.ArrayList;
import java.util.List;

public class Player implements Runnable{
    String name;
    Board board;
    List<Token> tokens;
    int numberOfJokers;

    public Player(String name, Board board) {
        this.name = name;
        this.board = board;
        this.tokens = new ArrayList<>();
    }

    public void run(){
        while(!board.isEmpty()){
            tokens.add(board.extract( this ));
        }
    }

    public String getName() {
        return name;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", tokens=" + tokens +
                '}';
    }
}
