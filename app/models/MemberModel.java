package models;

import io.ebean.Finder;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * MemberModel.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.17
 * @since 17.12.17
 */
@Entity
@Table(name = "member")
public class MemberModel extends BaseModel {

    /**
     * A Finder to simplify querying.
     *
     * @since 17.12.17
     */
    public static final Finder<Long, MemberModel> find = new Finder<>(MemberModel.class);

    /**
     * The member's username.
     *
     * @since 17.12.17
     */
    @Size(min = 3, max = 10)
    @Column(nullable = false, unique = true)
    private String username;

    /**
     * The member's password.
     *
     * @since 17.12.17
     */
    @Column(nullable = false)
    private String password;

    /**
     * The member's settings.
     *
     * @since 17.12.17
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "member_settings_uid", nullable = false, unique = true)
    private MemberSettingsModel memberSettings;

    /**
     * The list of cards owned by the member.
     *
     * @since 17.12.17
     */
    @ManyToMany(targetEntity = CardModel.class)
    @JoinTable(name = "member_owns_card")
    private List<CardModel> cards;

    /**
     * Create a new member.
     *
     * @since 17.12.18
     */
    public MemberModel() {
        this.cards = new ArrayList<>();
        this.memberSettings = new MemberSettingsModel();
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
     * Return the member's password.
     *
     * @return the member's password
     * @since 17.12.17
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Set the member's password.
     *
     * @param password the member's password
     * @since 17.12.17
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * Return the member's settings
     *
     * @return the member's settings
     * @since 17.12.17
     */
    public MemberSettingsModel getMemberSettings() {
        return this.memberSettings;
    }

    /**
     * Set the member's settings
     *
     * @param memberSettings the member's settings
     * @since 17.12.17
     */
    public void setMemberSettings(final MemberSettingsModel memberSettings) {
        this.memberSettings = memberSettings;
    }

    /**
     * Return the list of cards owned by the member.
     *
     * @return the list of cards owned by the member
     * @since 17.12.17
     */
    public List<CardModel> getCards() {
        return this.cards;
    }

    /**
     * Set the list of cards owned by the member.
     *
     * @param cards the list of cards owned by the member
     * @since 17.12.17
     */
    public void setCards(final List<CardModel> cards) {
        this.cards = cards;
    }
}
