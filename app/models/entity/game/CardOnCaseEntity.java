package models.entity.game;

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
     * Create a new CardOnCaseEntity based on a CardOnCaseModel.
     *
     * @param cardOnCaseModel the model of the entity
     * @since 17.12.24
     */
    public CardOnCaseEntity(final CardOnCaseModel cardOnCaseModel) {
        this.name = cardOnCaseModel.getCard().getName();
        this.cardOnCaseRef = cardOnCaseModel.getUid();
        this.playerRef = cardOnCaseModel.getPlayer().getUid();
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
}
