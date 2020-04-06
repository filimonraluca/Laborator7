package gameImpl.gameStrategy;

import gameImpl.Board;
import gameImpl.Token;

import java.util.Scanner;

public class ManualStrategy implements Strategy {
    Board board;

    public ManualStrategy(Board board){
        this.board = board;
    }

    /**
     * In aceasta metoda se afiseaza jucatorului tokenurile existente iar el pentru a selecta un token va introduce de la
     * tastatura pozitia tokenului pe care il doreste.
     * @return tokenul dorit
     */
    @Override
    public Token findToken() {
        System.out.printf("The Remaining tokens are: ");
        board.printTokens();
        Scanner scanner = new Scanner(System.in);
        int value = scanner.nextInt();
        return board.getTokens().get(value);
    }
}
