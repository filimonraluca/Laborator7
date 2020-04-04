package gameImpl;

public class TimeKeeper implements Runnable{

    private final Game game;
    private final Thread gameThread;
    private final float gameDuration;

    public TimeKeeper(Game game, Thread gameThread, int gameDuration ) {
        this.game = game;
        this.gameThread = gameThread;
        this.gameDuration = gameDuration/10;
    }

    @Override
    public void run() {
        System.out.println(gameDuration);
        for ( int i = 0; i < gameDuration && !game.getScoreManager().hasWinner(); ++i ) {
            try {
                Thread.sleep( 10 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("%s milliseconds passed\n", i * 10);
        }
        if (!game.getScoreManager().hasWinner()) {
            stopAll();
        }
    }

    private void stopAll() {
        gameThread.interrupt();
        for (Thread t:game.getPlayersThread()) {
            t.interrupt();
        }
    }
}
