package gameImpl.gameStrategy;

import gameImpl.Board;
import gameImpl.Token;

public class LastTokenStrategy implements Strategy {
    Board board;

    public LastTokenStrategy(Board board){
        this.board = board;
    }

    @Override
    public Token findToken() {
        return board.getTokens().get(board.getTokens().size() - 1);
    }
}
