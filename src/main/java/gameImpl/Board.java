package gameImpl;

import java.util.List;

/**
 * Clasa Board are ca si atribute lista tokenurilor prezente pe tabla si jucatorul castigator de la final
 */
public class Board {
    List<Token> tokens;

    public Board(List<Token> tokens) {
        this.tokens = tokens;
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
            if (!this.isEmpty()) {
                nextToken = tokens.remove(tokens.size() - 1);
            }
        }
        return nextToken;
    }

    public boolean isEmpty() {
        return tokens.isEmpty();
    }
}
