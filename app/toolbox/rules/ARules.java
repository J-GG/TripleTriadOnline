package toolbox.rules;

import models.game.CardOnCaseModel;
import models.game.GameModel;

/**
 * ARules.
 *
 * @author Jean-Gabriel Genest
 * @version 18.01.01
 * @since 18.01.01
 */
public abstract class ARules {

    protected final GameModel game;

    protected final int row;

    protected final int col;

    protected CardOnCaseModel card;

    public ARules(final GameModel game, final int row, final int col) {
        this.game = game;
        this.row = row;
        this.col = col;
        this.card = game.getBoard().getCase(row, col).getCardOnCase();
    }

    abstract void apply();

    public CardOnCaseModel getAboveCard() {
        return (this.row - 1 >= 0) ? this.game.getBoard().getCase(this.row - 1, this.col).getCardOnCase() : null;
    }

    public CardOnCaseModel getBelowCard() {
        return (this.row + 1 < this.game.getBoard().getNbRows()) ? this.game.getBoard().getCase(this.row + 1, this.col).getCardOnCase() : null;
    }

    public CardOnCaseModel getRightCard() {
        return (this.col + 1 < this.game.getBoard().getNbCols()) ? this.game.getBoard().getCase(this.row, this.col + 1).getCardOnCase() : null;
    }

    public CardOnCaseModel getLeftCard() {
        return (this.col - 1 >= 0) ? this.game.getBoard().getCase(this.row, this.col - 1).getCardOnCase() : null;
    }
}
