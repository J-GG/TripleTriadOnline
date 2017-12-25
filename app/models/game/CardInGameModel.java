package models.game;

import models.BaseModel;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

/**
 * CardInGameModel.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.23
 * @since 17.12.23
 */
@MappedSuperclass
public class CardInGameModel extends BaseModel {

    /**
     * The player owning card.
     *
     * @since 17.12.23
     */
    @ManyToOne(targetEntity = PlayerModel.class)
    @JoinColumn(nullable = false)
    private PlayerModel player;

    /**
     * The card.
     *
     * @since 17.12.23
     */
    @ManyToOne(targetEntity = CardModel.class)
    @JoinColumn(nullable = false)
    private CardModel card;

    /**
     * Create a new card in game with a player and a card.
     *
     * @param player the player owning the card
     * @param card   the card
     * @since 17.12.24
     */
    public CardInGameModel(final PlayerModel player, final CardModel card) {
        this.player = player;
        this.card = card;
    }

    /**
     * Return the card.
     *
     * @return the card in the deck
     * @since 17.12.23
     */
    public CardModel getCard() {
        return this.card;
    }

    /**
     * Set the card.
     *
     * @param card the card in the deck
     * @since 17.12.23
     */
    public void setCard(final CardModel card) {
        this.card = card;
    }

    /**
     * Return the player owning the card.
     *
     * @return the player owning the card
     * @since 17.12.23
     */
    public PlayerModel getPlayer() {
        return this.player;
    }

    /**
     * Set the player owning the card.
     *
     * @param player the player owning the card.
     * @since 17.12.23
     */
    public void setPlayer(final PlayerModel player) {
        this.player = player;
    }
}
