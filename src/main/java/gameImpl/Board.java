package gameImpl;

import java.util.List;

/**
 * Clasa Board are ca si atribute lista tokenurilor prezente pe tabla si jucatorul castigator de la final
 */
public class Board {
    List<Token> tokens;
    Player winner;
    Boolean isWinner = false;

    public Board(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Am folosit un synchronized statement pentru a preveni intrarea a mai multor threaduri in verificare
     * Practic, ne ajuta sa alegem un singur castigator la final in momentul in care s-a gasit.
     * @param player reprezinta obiectul de tip Player care devine jucator daca nici un alt thread nu a intrat acolo inca
     */
    public void claimWin(Player player) {
        synchronized (isWinner) {
            if (!isWinner) {
                winner = player;
                isWinner = true;
                System.out.printf("gameImpl.Player %s claimed it won\n", player.getName());
            } else {
                System.out.printf("gameImpl.Player %s claimed it won after player %s\n", player.getName(), winner.getName());
            }
        }
    }

    /**
     * Am folosit din nou un synchronized statement pentru a bloca o zona de memorie care ar fi putut produce erori
     * Practic, sincronizam accesul la extragerea cartilor astfel incat sa nu se intample ca doi jucatori sa extraga aceeasi carte
     * @param player reprezinta obiectul de tip Player care extrage cartea
     * @return returneaza token-ul extras cu succes
     */
    public Token extract(Player player) {
        Token nextToken = null;
        synchronized (tokens) {
            if (!isWinner && !this.isEmpty()) {
                nextToken = tokens.remove(tokens.size() - 1);
            }
        }
        return nextToken;
    }

    public boolean isEmpty() {
        return tokens.isEmpty();
    }
}
