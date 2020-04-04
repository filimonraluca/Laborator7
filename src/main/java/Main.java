import gameImpl.Game;
import gameImpl.TimeKeeper;

/**
 * clasa principala in care cream o instanta a jocului nostru si apelam metoda start() pentru crearea si inceperea threadurile
 */
public class Main {
    public static void main(String[] args) {
        Game game = new Game(2, 10, 30, 3);
        Thread gameThread = new Thread(game);
        Thread timekeeper = new Thread( new TimeKeeper( game, gameThread, 100000000 ));
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
