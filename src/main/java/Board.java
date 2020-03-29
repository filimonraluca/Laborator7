import javax.sound.midi.Soundbank;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Board {
    List<Token> tokens;
    Player winner;
    Boolean isWinner=false;

    public Board() {
    }

    public Board(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void claimWin(Player player) {
        synchronized (isWinner) {
            if (!isWinner) {
                winner = player;
                isWinner = true;
                System.out.printf("Player %s claimed it won\n", player.getName());
            } else {
                System.out.printf("Player %s claimed it won after player %s\n", player.getName(), winner.getName());
            }
        }
    }

    public Token extract( Player player )
    {
        Token nextToken = null;
        synchronized (tokens) {
            if ( !isWinner  && !this.isEmpty()) {
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
