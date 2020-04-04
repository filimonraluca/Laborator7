package gameImpl;

public class TimeKeeper implements Runnable{
    static final int interval = 100000;
    private final Game game;
    private final Thread gameThread;
    private final float gameDuration;

    public TimeKeeper(Game game, Thread gameThread, int gameDuration ) {
        this.game = game;
        this.gameThread = gameThread;
        this.gameDuration = gameDuration/interval;
    }

    @Override
    public void run() {
        for ( int i = 0; i < gameDuration && !game.getScoreManager().hasWinner(); ++i ) {
            try {
                Thread.sleep( interval );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("%s milliseconds passed\n", i * interval);
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
