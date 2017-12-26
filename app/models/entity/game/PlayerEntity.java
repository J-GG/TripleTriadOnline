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
 * @version 17.12.26
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
     * The identifier of the member.
     *
     * @since 17.12.24
     */
    private final UUID memberRef;

    /**
     * The member's username.
     *
     * @since 17.12.26
     */
    private final String username;

    /**
     * The list of cards in the player's deck.
     *
     * @since 17.12.24
     */
    private final List<CardInDeckEntity> deck;

    /**
     * The player's score
     *
     * @since 17.12.26
     */
    private final int score;

    /**
     * Create a new PlayerEntity based on a PlayerModel.
     *
     * @param playerModel the model of the entity
     * @since 17.12.24
     */
    public PlayerEntity(final PlayerModel playerModel) {
        this.playerRef = playerModel.getUid();
        this.memberRef = playerModel.getMember() != null ? playerModel.getMember().getUid() : null;
        this.username = playerModel.getMember() != null ? playerModel.getMember().getUsername() : null;
        this.deck = new ArrayList<>();
        for (final CardInDeckModel cardInDeck : playerModel.getDeck()) {
            this.deck.add(new CardInDeckEntity(cardInDeck));
        }
        this.score = playerModel.getScore();
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
     * Return the identifier of the member.
     *
     * @return the identifier of the member
     * @since 17.12.26
     */
    public UUID getMemberRef() {
        return this.memberRef;
    }

    /**
     * Return the member's username.
     *
     * @return the member's username
     * @since 17.12.26
     */
    public String getUsername() {
        return this.username;
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

    /**
     * Return the player's score.
     *
     * @return the player's score
     * @since 17.12.26
     */
    public int getScore() {
        return this.score;
    }
}
