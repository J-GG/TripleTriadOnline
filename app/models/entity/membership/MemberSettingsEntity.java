package models.entity.membership;

/**
 * MemberSettingsEntity.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.19
 * @since 17.12.19
 */
public class MemberSettingsEntity {

    /**
     * The language of the interface
     *
     * @since 17.12.17
     */
    private String language;

    /**
     * Whether audio is enabled
     *
     * @since 17.12.17
     */
    private boolean audioEnabled;

    /**
     * The default settings for the games.
     *
     * @since 17.12.19
     */
    private DefaultGameSettingsEntity defaultGameSettings;

    /**
     * Create empty settings.
     *
     * @since 17.12.19
     */
    public MemberSettingsEntity() {
        this.defaultGameSettings = new DefaultGameSettingsEntity();
    }

    /**
     * Return the language of the interface.
     *
     * @return the language of the interface
     * @since 17.12.17
     */
    public String getLanguage() {
        return this.language;
    }

    /**
     * Set the language of the interface.
     *
     * @param language the language of the interface
     * @since 17.12.17
     */
    public void setLanguage(final String language) {
        this.language = language;
    }

    /**
     * Return whether audio is enabled.
     *
     * @return true if audio is enabled
     * @since 17.12.17
     */
    public boolean isAudioEnabled() {
        return this.audioEnabled;
    }

    /**
     * Set whether audio is enabled.
     *
     * @param audioEnabled true if audio is enabled
     * @since 17.12.17
     */
    public void setAudioEnabled(final boolean audioEnabled) {
        this.audioEnabled = audioEnabled;
    }

    /**
     * Return the default settings for the games.
     *
     * @return the fault settings for the games
     * @since 17.12.18
     */
    public DefaultGameSettingsEntity getDefaultGameSettings() {
        return this.defaultGameSettings;
    }

    /**
     * Set the default settings for the games.
     *
     * @param defaultGameSettings the default settings for the games
     * @since 17.12.18
     */
    public void setDefaultGameSettings(final DefaultGameSettingsEntity defaultGameSettings) {
        this.defaultGameSettings = defaultGameSettings;
    }
}
