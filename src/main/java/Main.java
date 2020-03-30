import gameImpl.Game;

/**
 * clasa principala in care cream o instanta a jocului nostru si apelam metoda start() pentru crearea si inceperea threadurile
 */
public class Main {
    public static void main(String[] args) {
        Game game = new Game(4, 10, 30, 30);
        game.start();
    }
}
