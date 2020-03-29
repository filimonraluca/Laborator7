package gameImpl;

import java.util.List;

public class Board {
    List<Token> tokens;
    Player winner;
    Boolean isWinner=false;

    public Board(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void claimWin(Player player) {
        synchronized (isWinner) {
            if (!isWinner) {
                winner = player;
                isWinner = true;
                System.out.printf("gameImpl.Player %s claimed it won\n", player.getName());
            } else {
                System.out.printf("gameImpl.Player %s claimed it won after player %s\n", player.getName(), winner.getName());
            }
        }
    }

    public Token extract( Player player )
    {
        Token nextToken = null;
        synchronized (tokens) {
            if ( !isWinner  && !this.isEmpty()) {
                nextToken = tokens.remove(tokens.size() - 1);
            }
        }
        return nextToken;
    }

    public boolean isEmpty(){
        return tokens.isEmpty();
    }
}
