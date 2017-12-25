package models.game;

import io.ebean.Finder;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CardInDeckModel.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.23
 * @since 17.12.23
 */
@Entity
@Table(name = "card_in_deck")
public class CardInDeckModel extends CardInGameModel {

    /**
     * A Finder to simplify querying.
     *
     * @since 17.12.23
     */
    public static final Finder<Long, CardInDeckModel> find = new Finder<>(CardInDeckModel.class);

    /**
     * Create a new card in deck with a player and a card.
     *
     * @param player the player owning the card
     * @param card   the card
     * @since 17.12.24
     */
    public CardInDeckModel(final PlayerModel player, final CardModel card) {
        super(player, card);
    }
}
