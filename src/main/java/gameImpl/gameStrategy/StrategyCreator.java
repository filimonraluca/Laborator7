package gameImpl.gameStrategy;

import gameImpl.Board;
/**
 * Clasa StrategyCreator are o singura metoda, si anume create, prin intermediul careia va returna un obiect de tipul
 * Strategy care implementeaza strategia dorita.
 */
public class StrategyCreator {
    public static Strategy create(StrategyType strategyType, Board board) {
        switch (strategyType){
            case LAST_TOKEN:
                return new LastTokenStrategy(board);
            case RANDOM_TOKEN:
                return new RandomStrategy(board);
            case MANUAL:
                return new ManualStrategy(board);
            default:
                return null;
        }
    }
}
