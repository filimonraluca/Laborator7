package gameImpl.gameStrategy;

import gameImpl.Board;
import gameImpl.Token;

import java.util.Scanner;

public class ManualStrategy implements Strategy {
    Board board;

    public ManualStrategy(Board board){
        this.board = board;
    }

    @Override
    public Token findToken() {
        System.out.printf("The Remaining tokens are: ");
        board.printTokens();
        Scanner scanner = new Scanner(System.in);
        int value = scanner.nextInt();
        System.out.println("The input value is" + value);
        return board.getTokens().get(value);
    }
}
