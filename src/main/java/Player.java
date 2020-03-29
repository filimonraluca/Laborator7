import com.sun.tools.javac.parser.Tokens;

import java.util.*;

public class Player implements Runnable{
    String name;
    Board board;
    List<Token> tokens;
    int arithmeticSize;
    int numberOfJokers = 0;
    int score;


    public Player(String name, Board board, int k) {
        this.name = name;
        this.board = board;
        this.tokens = new ArrayList<>();
        this.arithmeticSize = k;
    }

    public void run(){
        boolean isDone=false;
        int length=0;
        while(!board.isEmpty() && !isDone) {
            tokens.add(board.extract(this));
            Token lastToken = tokens.get(tokens.size() - 1);
            if (lastToken.isJoker()) {
                numberOfJokers++;
            }
            length = maxLengthForProgression();
            if ( length >= arithmeticSize ) {
                isDone=true;
            }

            //score = progressionLenght();
        }
        if (isDone) {
            System.out.printf("I (%s) found a progression for the required length\n", name);
            board.claimWin(this);
        } else{
            System.out.printf("I (%s) did not find a progression for the required length, my max length is %d\n", name, length);
        }
    }

    public boolean find(int value)
    {
        for(Token t:tokens){
            if(t.getNumber()==value) return true;
        }
        return false;
    }

    public int maxLengthForProgression(){
        int maxLength = 0, length;
        int limit = computeMaximumRatio();
        for (int ratio=1; ratio<=limit; ++ratio) {
            length = maxLengthForRatio(ratio);
            if (maxLength < length) {
                maxLength = length;
            }
        }
        return maxLength;
    }

    private int maxLengthForRatio(int ratio) {
        int maxLength = 0, length;
        Token maxTokenStart = null;
        for (Token t:tokens)
            if (!t.isJoker() ) {
                length = progressionLengthStartingFrom(t, ratio);
                if (maxLength < length) {
                    maxLength = length;
                    maxTokenStart = t;
                }
            }
        System.out.printf("I (%s) found a progression of ratio %d and size %d starting from %d\n", name, ratio, maxLength, maxTokenStart.getNumber());
        return maxLength;
    }

    private int computeMaximumRatio() {
        Token maximum = Collections.max(tokens);
        Token minimum = Collections.min(tokens);
        return maximum.getNumber() - minimum.getNumber();
    }

    private int progressionLengthStartingFrom(Token start, int ratio) {
        int numberOfJokersLeft = numberOfJokers;
        int size = 1;
        Token previous = start, current;
        do {
            current = new Token(previous.getNumber() + ratio);
            if ( hasToken( current) ) {
                previous = current;
                ++size;
            } else if ( numberOfJokersLeft > 0 ) {
                --numberOfJokersLeft;
                previous = current;
                ++size;
            } else {
                previous=null;
            }
        } while ( previous != null);
        return size;
    }

    private boolean hasToken(Token current) {
        for (Token t : tokens) {
            if (current.getNumber() == t.getNumber()) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public List<Token> getTokens() {
        return tokens;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", tokens=" + tokens +
                ", numberOfJokers=" + numberOfJokers +
                '}';
    }
}
