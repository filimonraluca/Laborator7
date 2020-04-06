package gameImpl.gameStrategy;

import gameImpl.Board;
import gameImpl.Token;

import java.util.Random;

public class RandomStrategy implements Strategy{
    private final Random random;
    private final Board board;


    public RandomStrategy(Board board){
        this.board = board;
        this.random = new Random();
    }

    /**
     * In aceasta metoda se selecteaza in mod random un token
     * @return tokenul selectat
     */
    @Override
    public Token findToken() {
        return board.getTokens().get( random.nextInt(board.getTokens().size()) );
    }
}
