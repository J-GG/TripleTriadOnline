package models.entity.game;

import models.enumeration.RuleEnum;
import models.game.CardOnCaseModel;

import java.util.UUID;

/**
 * CardOnCaseEntity.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.24
 * @since 17.12.24
 */
public class CardOnCaseEntity {

    /**
     * The name of the card.
     *
     * @since 17.12.24
     */
    private final String name;

    /**
     * The identifier of the card on the case.
     *
     * @since 17.12.24
     */
    private final UUID cardOnCaseRef;

    /**
     * The owner of the card.
     *
     * @since 17.12.24
     */
    private final UUID playerRef;

    /**
     * The identifier of the card which flipped this card if it's flipped.
     *
     * @since 17.12.23
     */
    private final UUID flippedByCardRef;

    /**
     * The rule which has flipped this card if it's flipped.
     *
     * @since 17.12.23
     */
    private final RuleEnum flippedByRule;

    /**
     * The step in the flipping chain if it's flipped.
     *
     * @since 17.12.23
     */
    private final Integer flippingStep;

    /**
     * Create a new CardOnCaseEntity based on a CardOnCaseModel.
     *
     * @param cardOnCaseModel the model of the entity
     * @since 17.12.24
     */
    public CardOnCaseEntity(final CardOnCaseModel cardOnCaseModel) {
        this.name = cardOnCaseModel.getCard().getName();
        this.cardOnCaseRef = cardOnCaseModel.getUid();
        this.playerRef = cardOnCaseModel.getPlayer().getUid();
        this.flippedByCardRef = cardOnCaseModel.getFlippedByCard() != null ? cardOnCaseModel.getFlippedByCard().getUid() : null;
        this.flippedByRule = cardOnCaseModel.getFlippedByRule();
        this.flippingStep = cardOnCaseModel.getFlippingStep();
    }


    /**
     * Return the name of the card.
     *
     * @return the name of the card
     * @since 17.12.24
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return the identifier of the card on the case.
     *
     * @return the identifier of the card on the case
     * @since 17.12.24
     */
    public UUID getCardOnCaseRef() {
        return this.cardOnCaseRef;
    }

    /**
     * Return the identifier of the player owning the card.
     *
     * @return the identifier of the player owning the card
     * @since 17.12.24
     */
    public UUID getPlayerRef() {
        return this.playerRef;
    }

    /**
     * Return the identifier of the card by which this card has been flipped.
     *
     * @return the identifier of the card by which this card has been flipped or null if it's not flipped
     * @since 17.12.24
     */
    public UUID getFlippedByCardRef() {
        return this.flippedByCardRef;
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
     * Return the step in the flipping chain.
     *
     * @return the step in the flipping chain or null if it's not flipped
     * @since 17.12.24
     */
    public Integer getFlippingStep() {
        return this.flippingStep;
    }
}
