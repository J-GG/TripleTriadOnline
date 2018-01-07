package toolbox.rules;

import models.enumeration.RuleEnum;
import models.game.CardOnCaseModel;
import models.game.CaseModel;
import models.game.GameModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * SameRule.
 *
 * @author Jean-Gabriel Genest
 * @version 18.01.06
 * @since 18.01.06
 */
public class SameRule extends ARule {

    /**
     * The number of cards having the same same facing value.
     *
     * @since 18.01.06
     */
    private int nbOfSameValues;

    /**
     * Create a new Same Rule.
     *
     * @param game      the game
     * @param step      the step in the flipping chain
     * @param caseModel the case triggering the rule
     * @since 18.01.06
     */
    public SameRule(final GameModel game, final int step, final CaseModel caseModel) {
        super(game, RuleEnum.SAME, step, caseModel);
    }

    @Override
    public List<CardOnCaseModel> test() {
        this.nbOfSameValues = 0;
        final List<CardOnCaseModel> flippedCards = new ArrayList<>();

        if (getAboveCard() != null) {
            flippedCards.add(compare(this.cardOnCase.getCard().getTopValue(), getAboveCard(), getAboveCard().getCard().getBottomValue()));
        }
        if (getBelowCard() != null) {
            flippedCards.add(compare(this.cardOnCase.getCard().getBottomValue(), getBelowCard(), getBelowCard().getCard().getTopValue()));
        }
        if (getLeftCard() != null) {
            flippedCards.add(compare(this.cardOnCase.getCard().getLeftValue(), getLeftCard(), getLeftCard().getCard().getRightValue()));
        }
        if (getRightCard() != null) {
            flippedCards.add(compare(this.cardOnCase.getCard().getRightValue(), getRightCard(), getRightCard().getCard().getLeftValue()));
        }

        if (this.nbOfSameValues < 2) {
            flippedCards.clear();
        }

        flippedCards.removeIf(Objects::isNull);

        return flippedCards;
    }

    /**
     * Compare the values of the cards according to the rule.
     *
     * @param cardValue          the card value
     * @param cardToCompare      the card on case to compare
     * @param cardToCompareValue the value to compare
     * @return the card on case if the rule can flip it
     * @since 18.01.06
     */
    private CardOnCaseModel compare(final Integer cardValue, final CardOnCaseModel cardToCompare, final Integer cardToCompareValue) {
        if (cardToCompareValue.equals(cardValue)) {
            this.nbOfSameValues++;
            if (!cardToCompare.getPlayer().getUid().equals(this.cardOnCase.getPlayer().getUid())) {
                return cardToCompare;
            }
        }

        return null;
    }
}
