package models.game;

import models.enumeration.RuleEnum;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * CardOnCaseModel.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.23
 * @since 17.12.23
 */
@Entity
@Table(name = "card_on_case")
public class CardOnCaseModel extends CardInGameModel {

    /**
     * The case on which the card is.
     *
     * @since 17.12.24
     */
    @OneToOne(targetEntity = CaseModel.class, mappedBy = "cardOnCase")
    private CaseModel caseModel;

    /**
     * The card which flipped this card if it's flipped.
     *
     * @since 17.12.23
     */
    private CardOnCaseModel flippedByCard;

    /**
     * The rule which has flipped this card if it's flipped.
     *
     * @since 17.12.23
     */
    private RuleEnum flippedByRule;

    /**
     * The step in the flipping chain if it's flipped.
     *
     * @since 17.12.23
     */
    private Integer flippingStep;

    /**
     * Create a new card on case with a player and a card.
     *
     * @param player the player owning the card
     * @param card   the card played
     * @since 17.12.24
     */
    public CardOnCaseModel(final PlayerModel player, final CardModel card) {
        super(player, card);
    }

    /**
     * Return the case on which the card is.
     *
     * @return the case on which the card is
     * @since 17.12.24
     */
    public CaseModel getCaseModel() {
        return this.caseModel;
    }

    /**
     * Set the case on which the card is.
     *
     * @param caseModel the case on which the card is
     * @since 17.12.24
     */
    public void setCaseModel(final CaseModel caseModel) {
        this.caseModel = caseModel;
    }

    /**
     * Return the card by which this card has been flipped.
     *
     * @return the card by which this card has been flipped or null if it's not flipped
     * @since 17.12.24
     */
    public CardOnCaseModel getFlippedByCard() {
        return this.flippedByCard;
    }

    /**
     * Return the card by which this card has been flipped.
     *
     * @param flippedByCard the card by which this card has been flipped
     * @since 17.12.24
     */
    public void setFlippedByCard(final CardOnCaseModel flippedByCard) {
        this.flippedByCard = flippedByCard;
    }

    /**
     * Return the rule which has flipped this card.
     *
     * @return the rule which has flipped this card or null if it's not flipped
     * @since 17.12.24
     */
    public RuleEnum getFlippedByRule() {
        return this.flippedByRule;
    }

    /**
     * Set the rule which has flipped this card.
     *
     * @param flippedByRule the rule which has flipped this card
     * @since 17.12.24
     */
    public void setFlippedByRule(final RuleEnum flippedByRule) {
        this.flippedByRule = flippedByRule;
    }

    /**
     * Return the step in the flipping chain.
     *
     * @return the step in the flipping chain or null if it's not flipped
     * @since 17.12.24
     */
    public int getFlippingStep() {
        return this.flippingStep;
    }

    /**
     * Set the step in the flipping chain.
     *
     * @param flippingStep the step in the flipping chain
     * @since 17.12.24
     */
    public void setFlippingStep(final Integer flippingStep) {
        this.flippingStep = flippingStep;
    }
}
