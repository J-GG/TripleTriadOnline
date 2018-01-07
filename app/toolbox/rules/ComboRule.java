package toolbox.rules;

import models.enumeration.RuleEnum;
import models.game.CardOnCaseModel;
import models.game.CaseModel;
import models.game.GameModel;

import java.util.ArrayList;
import java.util.List;

/**
 * ComboRule.
 *
 * @author Jean-Gabriel Genest
 * @version 18.01.06
 * @since 18.01.06
 */
public class ComboRule extends ARule {

    /**
     * Create a new Combo Rule.
     *
     * @param game      the game
     * @param step      the step in the flipping chain
     * @param caseModel the case triggering the rule
     * @since 18.01.06
     */
    public ComboRule(final GameModel game, final int step, final CaseModel caseModel) {
        super(game, RuleEnum.COMBO, step, caseModel);
    }

    /**
     * Return the list of cards on case eligible to the combo rule.
     *
     * @return the list of cards on case eligible to the combo rule
     * @since 18.01.06
     */
    private List<CardOnCaseModel> listComboEligibleCards() {
        final List<CardOnCaseModel> comboEligibleCards = new ArrayList<>();
        for (final CaseModel caseModel : this.game.getBoard().getCases()) {
            if (caseModel.getCardOnCase() != null && (RuleEnum.SAME.equals(caseModel.getCardOnCase().getFlippedByRule()) || RuleEnum.PLUS.equals(caseModel.getCardOnCase().getFlippedByRule()))) {
                comboEligibleCards.add(caseModel.getCardOnCase());
            }
        }

        return comboEligibleCards;
    }

    @Override
    public List<CardOnCaseModel> apply() {
        return recursiveCombo(listComboEligibleCards(), true);
    }

    @Override
    public List<CardOnCaseModel> test() {
        return recursiveCombo(listComboEligibleCards(), false);
    }

    /**
     * Apply or test the rule recursively.
     *
     * @param cards the list of cards on which the rule should be applied/tested
     * @param apply whether the rule should be applied or tested. True if it should be applied
     * @return the list of cards the rule can flip or has flipped
     * @since 18.01.06
     */
    private List<CardOnCaseModel> recursiveCombo(final List<CardOnCaseModel> cards, final boolean apply) {
        final List<CardOnCaseModel> flippedCards = new ArrayList<>();

        for (final CardOnCaseModel cardOnCase : cards) {
            final SimpleRule simpleRule = new SimpleRule(this.game, this.step, cardOnCase.getCaseModel(), this.rule);
            final List<CardOnCaseModel> flippedBySimple = apply ? simpleRule.apply() : simpleRule.test();
            if (flippedBySimple.size() > 0) {
                this.step++;
                flippedCards.addAll(flippedBySimple);
                flippedCards.addAll(recursiveCombo(flippedBySimple, apply));
            }
        }

        return flippedCards;
    }
}
