package models;

import io.ebean.Finder;

import javax.persistence.*;
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
    private String username;

    /**
     * The member's password.
     *
     * @since 17.12.17
     */
    private String password;

    /**
     * The list of cards owned by the member.
     *
     * @since 17.12.17
     */
    @ManyToMany(targetEntity = CardModel.class, fetch = FetchType.LAZY)
    @JoinTable(name = "member_owns_card")
    private List<CardModel> cards;

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
}
