package gameImpl;

public class TimeKeeper implements Runnable{
    final int interval;
    private final Game game;
    private final Thread gameThread;
    private final float gameDuration;

    public TimeKeeper(Game game, Thread gameThread, int gameDuration, int interval) {
        this.game = game;
        this.gameThread = gameThread;
        this.interval = interval;
        this.gameDuration = gameDuration/interval;
    }

    /**
     * Metoda run() este cea apelata in momentul in care se incepe executia threadredului. Cat timp nu exista inca un castigator
     * si timpul alocat jocului nu a expirat se foloseste metoda sleep pentru a opri executia thredului pe durata unui interval.
     * Daca durata jocului a expirat se apeleaza metoda stopAll pentru a termina executia tuturor playerilor.
     */
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

    /**
     * Aceasta metoda trimite semnal de intrerupere tuturor thredurilor jucatorilor. Daca thredul jucatorului este in wait
     * acesta va arunca exceptia InterruptedException.
     */
    private void stopAll() {
        gameThread.interrupt();
        for (Thread t:game.getPlayersThread()) {
            t.interrupt();
        }
    }
}
