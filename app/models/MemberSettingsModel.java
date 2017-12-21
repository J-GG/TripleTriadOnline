package models;

import io.ebean.Finder;

import javax.persistence.*;

/**
 * MemberSettingsModel.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.17
 * @since 17.12.17
 */
@Entity
@Table(name = "member_settings")
public class MemberSettingsModel extends BaseModel {

    /**
     * A Finder to simplify querying.
     *
     * @since 17.12.17
     */
    public static final Finder<Long, MemberSettingsModel> find = new Finder<>(MemberSettingsModel.class);

    /**
     * The language of the interface
     *
     * @since 17.12.17
     */
    @Column(nullable = false)
    private String language;

    /**
     * Whether audio is enabled
     *
     * @since 17.12.17
     */
    @Column(nullable = false)
    private boolean audioEnabled;

    /**
     * The default settings for the games.
     *
     * @since 17.12.18
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "default_game_settings_uid", nullable = false, unique = true)
    private DefaultGameSettingsModel defaultGameSettings;

    /**
     * The member to whom the settings are applied.
     *
     * @since 17.12.17
     */
    @OneToOne(targetEntity = MemberModel.class, mappedBy = "memberSettings", optional = false, cascade = CascadeType.REMOVE)
    private MemberModel member;

    /**
     * Create the member's settings.
     *
     * @since 17.12.18
     */
    public MemberSettingsModel() {
        this.audioEnabled = true;
        this.defaultGameSettings = new DefaultGameSettingsModel();
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
    public DefaultGameSettingsModel getDefaultGameSettings() {
        return this.defaultGameSettings;
    }

    /**
     * Set the default settings for the games.
     *
     * @param defaultGameSettings the default settings for the games
     * @since 17.12.18
     */
    public void setDefaultGameSettings(final DefaultGameSettingsModel defaultGameSettings) {
        this.defaultGameSettings = defaultGameSettings;
    }

    /**
     * Return the member to whom the settings are applied.
     *
     * @return the member to whom the settings are applied.
     * @since 17.12.17
     */
    public MemberModel getMember() {
        return this.member;
    }

    /**
     * Set the member to whom the settings are applied.
     *
     * @param member the member to whom the settings are applied.
     * @since 17.12.17
     */
    public void setMember(final MemberModel member) {
        this.member = member;
    }
}
