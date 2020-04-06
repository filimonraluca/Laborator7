import gameImpl.Game;
import gameImpl.TimeKeeper;
import gameImpl.gameStrategy.StrategyType;

/**
 * clasa principala am creat doua threaduri, unul pentru game si unul pentru timekeeper.
 * Am apelat pentru amandoua metoda start pentru a porni thredurile.
 */
public class Main {
    public static void main(String[] args) {
        Game game = new Game(2, 10, 30, 3, StrategyType.RANDOM_TOKEN);
        Thread gameThread = new Thread(game);
        Thread timekeeper = new Thread( new TimeKeeper( game, gameThread, 100000, 1 ));
        gameThread.start();
        timekeeper.start();
        try {
            gameThread.join();
            timekeeper.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
