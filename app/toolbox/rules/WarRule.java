package toolbox.rules;

import models.enumeration.RuleEnum;
import models.game.CardOnCaseModel;
import models.game.CaseModel;
import models.game.GameModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * WarRule.
 *
 * @author Jean-Gabriel Genest
 * @version 18.01.06
 * @since 18.01.06
 */
public class WarRule extends ARule {

    /**
     * Create a new War Rule.
     *
     * @param game      the game
     * @param step      the step in the flipping chain
     * @param caseModel the case triggering the rule
     * @since 18.01.06
     */
    public WarRule(final GameModel game, final int step, final CaseModel caseModel) {
        super(game, RuleEnum.WAR, step, caseModel);
    }

    @Override
    public List<CardOnCaseModel> test() {
        final List<CardOnCaseModel> flippedCards = new ArrayList<>();

        final Integer sum = this.cardOnCase.getCard().getBottomValue() + this.cardOnCase.getCard().getRightValue() + this.cardOnCase.getCard().getLeftValue() + this.cardOnCase.getCard().getTopValue();

        if (getAboveCard() != null) {
            flippedCards.add(compare(sum, this.cardOnCase.getCard().getTopValue(), getAboveCard(), getAboveCard().getCard().getBottomValue()));
        }
        if (getBelowCard() != null) {
            flippedCards.add(compare(sum, this.cardOnCase.getCard().getBottomValue(), getBelowCard(), getBelowCard().getCard().getTopValue()));
        }
        if (getLeftCard() != null) {
            flippedCards.add(compare(sum, this.cardOnCase.getCard().getLeftValue(), getLeftCard(), getLeftCard().getCard().getRightValue()));
        }
        if (getRightCard() != null) {
            flippedCards.add(compare(sum, this.cardOnCase.getCard().getRightValue(), getRightCard(), getRightCard().getCard().getLeftValue()));
        }

        flippedCards.removeIf(Objects::isNull);

        return flippedCards;
    }

    /**
     * Compare the values of the cards according to the rule.
     *
     * @param sum                the sum of the value of the card
     * @param cardValue          the card value
     * @param cardToCompare      the card on case to compare
     * @param cardToCompareValue the value to compare
     * @return the card on case if the rule can flip it
     * @since 18.01.06
     */
    private CardOnCaseModel compare(final Integer sum, final Integer cardValue, final CardOnCaseModel cardToCompare, final Integer cardToCompareValue) {
        if (!cardToCompare.getPlayer().getUid().equals(this.cardOnCase.getPlayer().getUid()) && cardToCompareValue.equals(cardValue)) {
            final Integer comparedSum = cardToCompare.getCard().getBottomValue() + cardToCompare.getCard().getRightValue() + cardToCompare.getCard().getLeftValue() + cardToCompare.getCard().getTopValue();
            if (comparedSum < sum) {
                return cardToCompare;
            }
        }

        return null;
    }
}
