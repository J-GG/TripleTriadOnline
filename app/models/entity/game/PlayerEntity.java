package models.entity.game;

import models.game.CardInDeckModel;
import models.game.PlayerModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * PlayerEntity.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.24
 * @since 17.12.24
 */
public class PlayerEntity {

    /**
     * The identifier of the player.
     *
     * @since 17.12.24
     */
    private final UUID playerRef;

    /**
     * The list of cards in the player's deck.
     *
     * @since 17.12.24
     */
    private final List<CardInDeckEntity> deck;

    /**
     * Create a new PlayerEntity based on a PlayerModel.
     *
     * @param playerModel the model of the entity
     * @since 17.12.24
     */
    public PlayerEntity(final PlayerModel playerModel) {
        this.playerRef = playerModel.getUid();
        this.deck = new ArrayList<>();
        for (final CardInDeckModel cardInDeck : playerModel.getDeck()) {
            this.deck.add(new CardInDeckEntity(cardInDeck));
        }
    }

    /**
     * Return the identifier of the player.
     *
     * @return the identifier of the player
     * @since 17.12.24
     */
    public UUID getPlayerRef() {
        return this.playerRef;
    }

    /**
     * Return the list of cards in the player's deck.
     *
     * @return the list of cards in the player's deck
     * @since 17.12.24
     */
    public List<CardInDeckEntity> getDeck() {
        return this.deck;
    }
}
