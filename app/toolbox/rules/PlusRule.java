package toolbox.rules;

import models.enumeration.RuleEnum;
import models.game.CardOnCaseModel;
import models.game.CaseModel;
import models.game.GameModel;

import java.util.*;

/**
 * PlusRule.
 *
 * @author Jean-Gabriel Genest
 * @version 18.01.06
 * @since 18.01.06
 */
public class PlusRule extends ARule {

    /**
     * The Map of sums with the list of cards corresponding to this sum.
     *
     * @since 18.01.06
     */
    private Map<Integer, List<CardOnCaseModel>> sums;

    /**
     * Create a new Plus Rule.
     *
     * @param game      the game
     * @param step      the step in the flipping chain
     * @param caseModel the case triggering the rule
     * @since 18.01.06
     */
    public PlusRule(final GameModel game, final int step, final CaseModel caseModel) {
        super(game, RuleEnum.PLUS, step, caseModel);
    }

    @Override
    public List<CardOnCaseModel> test() {
        final List<CardOnCaseModel> flippedCards = new ArrayList<>();
        this.sums = new HashMap<>();

        if (getAboveCard() != null) {
            sum(this.cardOnCase.getCard().getTopValue(), getAboveCard(), getAboveCard().getCard().getBottomValue());
        }
        if (getBelowCard() != null) {
            sum(this.cardOnCase.getCard().getBottomValue(), getBelowCard(), getBelowCard().getCard().getTopValue());
        }
        if (getLeftCard() != null) {
            sum(this.cardOnCase.getCard().getLeftValue(), getLeftCard(), getLeftCard().getCard().getRightValue());
        }
        if (getRightCard() != null) {
            sum(this.cardOnCase.getCard().getRightValue(), getRightCard(), getRightCard().getCard().getLeftValue());
        }

        this.sums.forEach((sum, listCardOnCase) -> {
            if (listCardOnCase.size() >= 2) {
                listCardOnCase.forEach(cardOnCase -> {
                    if (!cardOnCase.getPlayer().getUid().equals(this.cardOnCase.getPlayer().getUid())) {
                        flippedCards.add(cardOnCase);
                    }
                });
            }
        });

        flippedCards.removeIf(Objects::isNull);

        return flippedCards;
    }

    /**
     * Sum the values of the cards.
     *
     * @param cardValue          the card value
     * @param cardToCompare      the card on case to compare
     * @param cardToCompareValue the value to compare
     * @since 18.01.06
     */
    private void sum(final Integer cardValue, final CardOnCaseModel cardToCompare, final Integer cardToCompareValue) {
        final Integer sum = cardValue + cardToCompareValue;
        if (!this.sums.containsKey(sum)) {
            this.sums.put(sum, new ArrayList<>());
        }
        this.sums.get(sum).add(cardToCompare);
    }
}
