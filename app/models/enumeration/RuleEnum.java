package models.enumeration;

import io.ebean.annotation.EnumValue;

/**
 * RuleEnum.
 *
 * @author Jean-Gabriel Genest
 * @version 18.01.01
 * @since 17.12.17
 */
public enum RuleEnum {

    /**
     * The SIMPLE rule.
     *
     * @since 17.12.17
     */
    @EnumValue("SIMPLE")
    SIMPLE(3),

    /**
     * The OPEN rule.
     *
     * @since 17.12.17
     */
    @EnumValue("OPEN")
    OPEN(-1),

    /**
     * The WAR rule.
     *
     * @since 17.12.17
     */
    @EnumValue("WAR")
    WAR(4),

    /**
     * The SAME rule.
     *
     * @since 17.12.17
     */
    @EnumValue("SAME")
    SAME(1),

    /**
     * The PLUS rule.
     *
     * @since 17.12.17
     */
    @EnumValue("PLUS")
    PLUS(2),

    /**
     * The COMBO rule.
     *
     * @since 17.12.17
     */
    @EnumValue("COMBO")
    COMBO(5),;

    /**
     * The order in the application of the rules.
     *
     * @since 18.01.01
     */
    private final int order;

    /**
     * Create a new RuleEnum.
     *
     * @param order the order in the application of the rules
     * @since 18.01.01
     */
    RuleEnum(final int order) {
        this.order = order;
    }

    /**
     * Return the order in the application of the rules.
     *
     * @return the order in the application of the rules
     * @since 18.01.01
     */
    public int getOrder() {
        return this.order;
    }
}
