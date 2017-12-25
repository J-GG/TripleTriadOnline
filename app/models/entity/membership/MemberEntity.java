package models.entity.membership;

import models.membership.DefaultGameSettingsModel;
import models.membership.MemberModel;

import java.util.stream.Collectors;

/**
 * MemberEntity.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.19
 * @since 17.12.19
 */
public class MemberEntity {

    /**
     * The language of the interface.
     *
     * @since 17.12.19
     */
    private String username;

    /**
     * Whether audio is enabled.
     *
     * @since 17.12.19
     */
    private MemberSettingsEntity memberSettings;

    /**
     * Create an empty member.
     *
     * @since 17.12.19
     */
    public MemberEntity() {
        this.memberSettings = new MemberSettingsEntity();
    }

    /**
     * Create a new MemberEntity based on a MemberModel.
     *
     * @param memberModel the model of the entity
     * @since 17.12.19
     */
    public MemberEntity(final MemberModel memberModel) {
        this.username = memberModel.getUsername();
        this.memberSettings = new MemberSettingsEntity();
        this.memberSettings.setLanguage(memberModel.getMemberSettings().getLanguage());
        this.memberSettings.setAudioEnabled(memberModel.getMemberSettings().isAudioEnabled());
        final DefaultGameSettingsModel defaultGameSettingsModel = memberModel.getMemberSettings().getDefaultGameSettings();
        this.memberSettings.getDefaultGameSettings().setDifficulty(defaultGameSettingsModel.getDifficulty().name());
        this.memberSettings.getDefaultGameSettings().setEnabledRules(defaultGameSettingsModel.getEnabledRules().stream().map(Enum::name).collect(Collectors.toList()));
    }

    /**
     * Return the member's username.
     *
     * @return the member's username
     * @since 17.12.17
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Set the member's password.
     *
     * @param username the member's password
     * @since 17.12.17
     */
    public void setUsername(final String username) {
        this.username = username;
    }

    /**
     * Return the member's settings
     *
     * @return the member's settings
     * @since 17.12.17
     */
    public MemberSettingsEntity getMemberSettings() {
        return this.memberSettings;
    }

    /**
     * Set the member's settings
     *
     * @param memberSettings the member's settings
     * @since 17.12.17
     */
    public void setMemberSettings(final MemberSettingsEntity memberSettings) {
        this.memberSettings = memberSettings;
    }
}
