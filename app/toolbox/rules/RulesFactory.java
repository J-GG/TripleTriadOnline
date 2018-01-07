package toolbox.rules;

import models.enumeration.RuleEnum;
import models.game.CardOnCaseModel;
import models.game.CaseModel;
import models.game.GameModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * RulesFactory.
 *
 * @author Jean-Gabriel Genest
 * @version 18.01.01
 * @since 18.01.01
 */
public class RulesFactory {

    /**
     * Apply the rules of the game on the case.
     *
     * @param game      the game containing the rules
     * @param caseModel the case on which the rules are applied
     * @since 18.01.06
     */
    public static void applyRules(final GameModel game, final CaseModel caseModel) {
        List<RuleEnum> enabledRules = new ArrayList<>(game.getEnabledRules());
        enabledRules.add(RuleEnum.SIMPLE);
        enabledRules = enabledRules.stream()
                .filter(rule -> rule.getOrder() != -1)
                .sorted((rule1, rule2) -> rule1.getOrder() > rule2.getOrder() ? rule1.getOrder() : rule2.getOrder())
                .collect(Collectors.toList());
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
                final List<CardOnCaseModel> flippedCards = rule.apply();
                if (flippedCards.size() > 0) {
                    step++;
                }
            }
        }
    }
}
