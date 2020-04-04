import gameImpl.Game;
import gameImpl.TimeKeeper;

/**
 * clasa principala in care cream o instanta a jocului nostru si apelam metoda start() pentru crearea si inceperea threadurile
 */
public class Main {
    public static void main(String[] args) {
        Game game = new Game(2, 1000, 3000, 10);
        Thread gameThread = new Thread(game);
        Thread timekeeper = new Thread( new TimeKeeper( game, gameThread, 1000000 ));
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
