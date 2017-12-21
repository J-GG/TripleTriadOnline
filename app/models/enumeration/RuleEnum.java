package models.enumeration;

import io.ebean.annotation.EnumValue;

/**
 * RuleEnum.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.17
 * @since 17.12.17
 */
public enum RuleEnum {

    /**
     * The SIMPLE rule.
     *
     * @since 17.12.17
     */
    @EnumValue("SIMPLE")
    SIMPLE,

    /**
     * The OPEN rule.
     *
     * @since 17.12.17
     */
    @EnumValue("OPEN")
    OPEN,

    /**
     * The WAR rule.
     *
     * @since 17.12.17
     */
    @EnumValue("WAR")
    WAR,

    /**
     * The SAME rule.
     *
     * @since 17.12.17
     */
    @EnumValue("SAME")
    SAME,

    /**
     * The PLUS rule.
     *
     * @since 17.12.17
     */
    @EnumValue("PLUS")
    PLUS,

    /**
     * The COMBO rule.
     *
     * @since 17.12.17
     */
    @EnumValue("COMBO")
    COMBO,
}
