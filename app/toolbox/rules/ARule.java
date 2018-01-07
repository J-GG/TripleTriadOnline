package toolbox.rules;

import models.enumeration.RuleEnum;
import models.game.CardOnCaseModel;
import models.game.CaseModel;
import models.game.GameModel;

import java.util.List;

/**
 * ARule.
 *
 * @author Jean-Gabriel Genest
 * @version 18.01.06
 * @since 18.01.01
 */
public abstract class ARule {

    /**
     * The game in which the rule is applied.
     *
     * @since 18.01.06
     */
    protected final GameModel game;

    /**
     * The row of the case where the rule is applied.
     *
     * @since 18.01.06
     */
    private final int row;

    /**
     * The column of the case where the rule is applied.
     *
     * @since 18.01.06
     */
    private final int col;

    /**
     * The card triggering the rule.
     *
     * @since 18.01.06
     */
    protected CardOnCaseModel cardOnCase;

    /**
     * The step in the flipping chain.
     *
     * @since 18.01.06
     */
    protected int step;

    /**
     * The enum of rule.
     *
     * @since 18.01.06
     */
    protected final RuleEnum rule;

    /**
     * Create a new Rule.
     *
     * @param game      the game
     * @param rule      the enum corresponding to the rule
     * @param step      the step in the flipping chain
     * @param caseModel the case triggering the rule
     * @since 18.01.06
     */
    protected ARule(final GameModel game, final RuleEnum rule, final int step, final CaseModel caseModel) {
        this.game = game;
        this.rule = rule;
        this.step = step;
        this.row = caseModel.getRow();
        this.col = caseModel.getCol();
        this.cardOnCase = game.getBoard().getCase(this.row, this.col).getCardOnCase();
    }

    /**
     * Apply the rule and flip the cards.
     *
     * @return the list of flipped cards
     * @since 18.01.06
     */
    public List<CardOnCaseModel> apply() {
        final List<CardOnCaseModel> flippedCards = test();

        for (final CardOnCaseModel cardOnCase : flippedCards) {
            this.flipCard(cardOnCase);
        }

        return flippedCards;
    }

    /**
     * Test the rule.
     *
     * @return the list of cards which could get flipped
     * @since 18.01.06
     */
    public abstract List<CardOnCaseModel> test();

    /**
     * Flip the card.
     *
     * @param flippedCard the card to be flipped
     * @since 18.01.06
     */
    protected void flipCard(final CardOnCaseModel flippedCard) {
        flippedCard.setFlippingStep(this.step);
        flippedCard.setFlippedByCard(this.cardOnCase);
        flippedCard.setFlippedByRule(this.rule);
        flippedCard.setPlayer(this.cardOnCase.getPlayer());
    }

    /**
     * Return the card above the case.
     *
     * @return the card above the case or null if there is no card
     * @since 18.01.06
     */
    protected CardOnCaseModel getAboveCard() {
        return (this.row - 1 >= 0) ? this.game.getBoard().getCase(this.row - 1, this.col).getCardOnCase() : null;
    }

    /**
     * Return the card below the case.
     *
     * @return the card below the case or null if there is no card
     * @since 18.01.06
     */
    protected CardOnCaseModel getBelowCard() {
        return (this.row + 1 < this.game.getBoard().getNbRows()) ? this.game.getBoard().getCase(this.row + 1, this.col).getCardOnCase() : null;
    }

    /**
     * Return the card to the right of the case.
     *
     * @return the card to the right of the case or null if there is no card
     * @since 18.01.06
     */
    protected CardOnCaseModel getRightCard() {
        return (this.col + 1 < this.game.getBoard().getNbCols()) ? this.game.getBoard().getCase(this.row, this.col + 1).getCardOnCase() : null;
    }

    /**
     * Return the card to the left of the case.
     *
     * @return the card to the left of the case or null if there is no card
     * @since 18.01.06
     */
    protected CardOnCaseModel getLeftCard() {
        return (this.col - 1 >= 0) ? this.game.getBoard().getCase(this.row, this.col - 1).getCardOnCase() : null;
    }
}
