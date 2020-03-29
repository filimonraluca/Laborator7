import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Game {
    Board board;
    List<Player> players;
    List<Token> tokens;

    public Game( int playerNumber, int tokenNumber, int maxTokenValue ) {
        initTokens(tokenNumber,maxTokenValue);
        board   = new Board(tokens);
        players = new ArrayList<>();
        for ( int i=0; i<playerNumber; ++i){
            players.add( new Player( String.format( "Player %d", i+1 ), board ) );
        }
    }

    public void initTokens(int tokenNumber, int maxTokenValue){
        List<Integer> tokensValues = new ArrayList<>();
        for (Integer i = 0; i < maxTokenValue; i++)
            tokensValues.add(i);
        Collections.shuffle(tokensValues);

        int nrOfJokers = (int) (Math.random()*tokenNumber/2);
        tokens = new ArrayList<Token>();
        for ( int i=0; i<tokenNumber-nrOfJokers; ++i){
            tokens.add(new Token(tokensValues.get(i)));
        }
        for (int i=0; i<nrOfJokers; ++i){
            tokens.add(new Token(true));
        }
        Collections.shuffle(tokens);
    }

    public void start() {
        List<Thread> threads = new ArrayList<>();
        for (Player player: players) {
            Thread t = new Thread(player);
            t.start();
            threads.add(t);
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (Player p:players)
            System.out.println(p);
    }
}
