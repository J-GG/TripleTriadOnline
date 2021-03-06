package toolbox.rules;

import models.enumeration.RuleEnum;
import models.game.CardOnCaseModel;
import models.game.CaseModel;
import models.game.GameModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * SimpleRule.
 *
 * @author Jean-Gabriel Genest
 * @version 18.01.06
 * @since 18.01.01
 */
public class SimpleRule extends ARule {

    /**
     * Create a new Simple Rule.
     *
     * @param game      the game
     * @param step      the step in the flipping chain
     * @param caseModel the case triggering the rule
     * @since 18.01.06
     */
    public SimpleRule(final GameModel game, final int step, final CaseModel caseModel) {
        super(game, RuleEnum.SIMPLE, step, caseModel);
    }

    /**
     * Create a new Simple Rule with a custom rule value.
     *
     * @param game      the game
     * @param step      the step in the flipping chain
     * @param caseModel the case triggering the rule
     * @since 18.01.06
     */
    public SimpleRule(final GameModel game, final int step, final CaseModel caseModel, final RuleEnum rule) {
        super(game, rule, step, caseModel);
    }

    @Override
    public List<CardOnCaseModel> test() {
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
        if (!cardToCompare.getPlayer().getUid().equals(this.cardOnCase.getPlayer().getUid()) && cardToCompareValue < cardValue) {
            return cardToCompare;
        }

        return null;
    }

}
