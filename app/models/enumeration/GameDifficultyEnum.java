package models.enumeration;

import io.ebean.annotation.EnumValue;

/**
 * GameDifficultyEnum.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.17
 * @since 17.12.17
 */
public enum GameDifficultyEnum {

    /**
     * The lowest difficulty.
     *
     * @since 17.12.17
     */
    @EnumValue("EASY")
    EASY,

    /**
     * The normal difficulty.
     *
     * @since 17.12.17
     */
    @EnumValue("NORMAL")
    NORMAL,

    /**
     * The highest difficulty.
     *
     * @since 17.12.17
     */
    @EnumValue("HARD")
    HARD
}
