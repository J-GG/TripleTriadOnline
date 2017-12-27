package models.game;

import io.ebean.Finder;
import models.BaseModel;
import models.membership.MemberModel;

import javax.persistence.*;
import java.util.List;

/**
 * CardModel.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.27
 * @since 17.12.18
 */
@Entity
@Table(name = "card")
public class CardModel extends BaseModel {

    /**
     * A Finder to simplify querying.
     *
     * @since 17.12.23
     */
    public static final Finder<Long, CardModel> find = new Finder<>(CardModel.class);

    /**
     * The name of card
     *
     * @since 17.12.23
     */
    @Column(unique = true)
    private String name;

    /**
     * The level of the card.
     *
     * @since 17.12.23
     */
    private Integer level;

    /**
     * The value at the top of the card.
     *
     * @since 17.12.23
     */
    private Integer topValue;

    /**
     * The value on the right side of the card.
     *
     * @since 17.12.23
     */
    private Integer rightValue;

    /**
     * The value at the bottom of the card.
     *
     * @since 17.12.23
     */
    private Integer bottomValue;

    /**
     * The value on the left side of the card.
     *
     * @since 17.12.23
     */
    private Integer leftValue;

    /**
     * The maximum level of cards.
     *
     * @since 17.12.23
     */
    public static final int MAX_LEVEL = 1;

    /**
     * The list of members owning this card.
     *
     * @since 17.12.23
     */
    @ManyToMany(mappedBy = "cards", targetEntity = MemberModel.class)
    private List<MemberModel> owners;

    /**
     * The list of decks containing this card.
     *
     * @since 17.12.23
     */
    @OneToMany(mappedBy = "card")
    private List<CardInDeckModel> decks;

    /**
     * Return the name of the card.
     *
     * @return the name of the card
     * @since 17.12.23
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the name of the card.
     *
     * @param name the name of the card
     * @since 17.12.23
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Return the level of the card.
     *
     * @return the level of the card
     * @since 17.12.23
     */
    public Integer getLevel() {
        return this.level;
    }

    /**
     * Set the level of the card.
     *
     * @param level the level of the card
     * @since 17.12.23
     */
    public void setLevel(final Integer level) {
        this.level = level;
    }

    /**
     * Return the value at the top of the card.
     *
     * @return the value at the top of the card
     * @since 17.12.23
     */
    public Integer getTopValue() {
        return this.topValue;
    }

    /**
     * Set the value at the top of the card.
     *
     * @param topValue the value at the top of the card
     * @since 17.12.23
     */
    public void setTopValue(final Integer topValue) {
        this.topValue = topValue;
    }

    /**
     * Return the value on the right side of the card.
     *
     * @return the value on the right side of the card
     * @since 17.12.23
     */
    public Integer getRightValue() {
        return this.rightValue;
    }

    /**
     * Set the value on the right side of the card.
     *
     * @param rightValue the value on the right side of the card
     * @since 17.12.23
     */
    public void setRightValue(final Integer rightValue) {
        this.rightValue = rightValue;
    }

    /**
     * Return the value at the bottom of the card.
     *
     * @return the value at the bottom of the card
     * @since 17.12.23
     */
    public Integer getBottomValue() {
        return this.bottomValue;
    }

    /**
     * Set the value at the bottom of the card.
     *
     * @param bottomValue the value at the bottom of the card
     * @since 17.12.23
     */
    public void setBottomValue(final Integer bottomValue) {
        this.bottomValue = bottomValue;
    }

    /**
     * Return the value on the left side of the card.
     *
     * @return the value an the left side of the card
     * @since 17.12.23
     */
    public Integer getLeftValue() {
        return this.leftValue;
    }

    /**
     * Set the value on the left side of the card.
     *
     * @param leftValue the value on the left side of the card
     * @since 17.12.23
     */
    public void setLeftValue(final Integer leftValue) {
        this.leftValue = leftValue;
    }

    /**
     * Return the list of owners of this card.
     *
     * @return the list of owners of this card
     * @since 17.12.23
     */
    public List<MemberModel> getOwners() {
        return this.owners;
    }

}
