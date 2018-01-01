package toolbox.rules;

import models.game.GameModel;

/**
 * SimpleRule.
 *
 * @author Jean-Gabriel Genest
 * @version 18.01.01
 * @since 18.01.01
 */
public class SimpleRule extends ARules {

    public SimpleRule(final GameModel game, final int row, final int col) {
        super(game, row, col);
    }

    @Override
    public void apply() {
        if (this.getAboveCard() != null && !this.getAboveCard().getPlayer().getUid().equals(this.card.getPlayer().getUid()) && this.getAboveCard().getCard().getBottomValue() < this.card.getCard().getTopValue()) {
            System.out.println("APPLY");
        }
    }
}
