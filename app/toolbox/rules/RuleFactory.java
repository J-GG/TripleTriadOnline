package toolbox.rules;

import models.enumeration.RuleEnum;
import models.game.CardOnCaseModel;
import models.game.CaseModel;
import models.game.GameModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RuleFactory.
 *
 * @author Jean-Gabriel Genest
 * @version 18.01.08
 * @since 18.01.01
 */
public class RuleFactory {

    /**
     * Apply the rules of the game on the case.
     *
     * @param game      the game containing the rules
     * @param caseModel the case on which the rules are applied
     * @return the number of flipped cards
     * @since 18.01.06
     */
    public static int applyRules(final GameModel game, final CaseModel caseModel) {
        return executeRules(game, game.getEnabledRules(), caseModel, true);
    }

    /**
     * Test the rules of the game on the case.
     *
     * @param game      the game containing the rules
     * @param caseModel the case on which the rules are tested
     * @return the number of cards which could get flipped
     * @since 18.01.06
     */
    public static int testRules(final GameModel game, final CaseModel caseModel) {
        return executeRules(game, game.getEnabledRules(), caseModel, false);
    }

    /**
     * Test the given rules on the case.
     *
     * @param game      the game containing the rules
     * @param caseModel the case on which the rules are tested
     * @param rules     the rules to test
     * @return the number of cards which could get flipped
     * @since 18.01.06
     */
    public static int testRules(final GameModel game, final List<RuleEnum> rules, final CaseModel caseModel) {
        return executeRules(game, rules, caseModel, false);
    }

    /**
     * Apply or test the rules of the game on the case.
     *
     * @param game      the game containing the rules
     * @param caseModel the case on which the rules are tested
     * @param apply     true if the rules should be applied
     * @param rules     the rules to test
     * @return the number of cards which could get flipped
     * @since 18.01.06
     */
    private static int executeRules(final GameModel game, final List<RuleEnum> rules, final CaseModel caseModel, final boolean apply) {
        List<RuleEnum> enabledRules = new ArrayList<>(rules);
        enabledRules.add(RuleEnum.SIMPLE);
        enabledRules = enabledRules.stream()
                .filter(rule -> rule.getOrder() != -1)
                .sorted((rule1, rule2) -> rule1.getOrder() > rule2.getOrder() ? rule1.getOrder() : rule2.getOrder())
                .collect(Collectors.toList());
        int nbFlippedCards = 0;
        int step = 1;
        for (final RuleEnum ruleEnabled : enabledRules) {
            ARule rule = null;
            switch (ruleEnabled) {
                case SAME:
                    rule = new SameRule(game, step, caseModel);
                    break;
                case PLUS:
                    rule = new PlusRule(game, step, caseModel);
                    break;
                case SIMPLE:
                    rule = new SimpleRule(game, step, caseModel);
                    break;
                case WAR:
                    rule = new WarRule(game, step, caseModel);
                    break;
                case COMBO:
                    rule = new ComboRule(game, step, caseModel);
                    break;
            }
            if (rule != null) {
                final List<CardOnCaseModel> flippedCards = apply ? rule.apply() : rule.test();
                if (flippedCards.size() > 0) {
                    step++;
                    nbFlippedCards += flippedCards.size();
                }
            }
        }

        return nbFlippedCards;
    }
}
