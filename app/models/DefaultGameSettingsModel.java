package models;

import io.ebean.Finder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * DefaultGameSettingsModel.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.18
 * @since 17.12.18
 */
@Entity
@Table(name = "default_game_settings")
public class DefaultGameSettingsModel extends GameSettingsModel {

    /**
     * A Finder to simplify querying.
     *
     * @since 17.12.17
     */
    public static final Finder<Long, DefaultGameSettingsModel> find = new Finder<>(DefaultGameSettingsModel.class);

    /**
     * The memberSettings to whom the settings are applied.
     *
     * @since 17.12.17
     */
    @OneToOne(targetEntity = MemberSettingsModel.class, mappedBy = "defaultGameSettings", optional = false, cascade = CascadeType.REMOVE)
    private MemberSettingsModel memberSettings;

    /**
     * Return the member's settings to which the game settings are attached.
     *
     * @return the member's settings to which the game settings are attached.
     * @since 17.12.17
     */
    public MemberSettingsModel getMemberSettings() {
        return this.memberSettings;
    }

    /**
     * Set the member's settings to which the game settings are attached.
     *
     * @param memberSettings the member's settings to which the game settings are attached.
     * @since 17.12.17
     */
    public void setMemberSettings(final MemberSettingsModel memberSettings) {
        this.memberSettings = memberSettings;
    }
}
