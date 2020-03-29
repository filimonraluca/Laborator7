import javax.sound.midi.Soundbank;
import java.util.List;
import java.util.Set;

public class Board {
    List<Token> tokens;

    public Board() {
    }

    public Board(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Token extract( Player player )
    {
        Token nextToken = null;
        synchronized (tokens) {
            if (!this.isEmpty()) {
                nextToken = tokens.remove(tokens.size() - 1);
                //System.out.printf("Player %s took token %d\n", player.name, nextToken.number);
            }
        }
        return nextToken;
    }

    public boolean isEmpty(){
        return tokens.isEmpty();
    }
}
