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

    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * Am folosit din nou un synchronized statement pentru a bloca o zona de memorie care ar fi putut produce erori
     * Practic, sincronizam accesul la extragerea cartilor astfel incat sa nu se intample ca doi jucatori sa extraga aceeasi carte
     * @param token reprezinta token ce va fi extras de pe tabla
     * @return returneaza true daca acesta a putut fi extras si false in caz contrar
     */
    public boolean extract(Token token) {
        Token nextToken = null;
        synchronized (tokens) {
            if (!this.isEmpty()) {
                //System.err.println(token);
                return tokens.remove(token);
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return tokens.isEmpty();
    }
}
