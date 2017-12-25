package models.entity.game;

import models.game.CardInDeckModel;

import java.util.UUID;

/**
 * CardInDeckEntity.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.24
 * @since 17.12.24
 */
public class CardInDeckEntity {

    /**
     * The name of the card.
     *
     * @since 17.12.24
     */
    private final String name;

    /**
     * The identifier of the card in the deck.
     *
     * @since 17.12.24
     */
    private final UUID cardInDeckRef;

    /**
     * Create a new CardInDeckEntity based on a CardInDeckModel.
     *
     * @param cardInDeck the model of the entity
     * @since 17.12.24
     */
    public CardInDeckEntity(final CardInDeckModel cardInDeck) {
        this.name = cardInDeck.getCard().getName();
        this.cardInDeckRef = cardInDeck.getUid();
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
     * Return the identifier of the card in the deck.
     *
     * @return the identifier of the card in the deck
     * @since 17.12.24
     */
    public UUID getCardInDeckRef() {
        return this.cardInDeckRef;
    }
}
