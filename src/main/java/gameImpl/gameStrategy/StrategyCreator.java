package gameImpl.gameStrategy;

import gameImpl.Board;

public class StrategyCreator {
    public static Strategy create(StrategyType strategyType, Board board) {
        switch (strategyType){
            case LAST_TOKEN:
                return new LastTokenStrategy(board);
            case RANDOM_TOKEN:
                return new RandomStrategy(board);
            default:
                return null;
        }
    }
}
