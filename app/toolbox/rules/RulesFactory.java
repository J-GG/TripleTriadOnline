package toolbox.rules;

import models.enumeration.RuleEnum;
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

    public static void applyRules(final GameModel game, final int row, final int col) {
        List<RuleEnum> enabledRules = new ArrayList<>(game.getEnabledRules());
        enabledRules.add(RuleEnum.SIMPLE);
        enabledRules = enabledRules.stream()
                .filter(rule -> rule.getOrder() != -1)
                .sorted((rule1, rule2) -> rule1.getOrder() > rule2.getOrder() ? rule1.getOrder() : rule2.getOrder())
                .collect(Collectors.toList());
        for (final RuleEnum ruleEnabled : enabledRules) {
            ARules rule = null;
            switch (ruleEnabled) {
                case SAME:
                    break;
                case PLUS:
                    break;
                case SIMPLE:
                    rule = new SimpleRule(game, row, col);
                    break;
                case WAR:
                    break;
                case COMBO:
                    break;
            }
            if (rule != null) {
                rule.apply();
            }
        }
    }
}
