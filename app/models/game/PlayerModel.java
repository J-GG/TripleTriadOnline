package models.game;

import io.ebean.Finder;
import models.BaseModel;
import models.membership.MemberModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * PlayerModel.
 *
 * @author Jean-Gabriel Genest
 * @version 17.12.23
 * @since 17.12.23
 */
@Entity
@Table(name = "player")
public class PlayerModel extends BaseModel {

    /**
     * A Finder to simplify querying.
     *
     * @since 17.12.23
     */
    public static final Finder<Long, PlayerModel> find = new Finder<>(PlayerModel.class);

    /**
     * The member who is playing.
     *
     * @since 17.12.23
     */
    @ManyToOne(targetEntity = MemberModel.class)
    private MemberModel member;

    /**
     * The game the player is playing.
     *
     * @since 17.12.23
     */
    @ManyToOne(targetEntity = GameModel.class)
    @JoinColumn(nullable = false)
    private GameModel game;

    /**
     * The list of cards in the player's deck.
     *
     * @since 17.12.23
     */
    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<CardInDeckModel> deck;

    /**
     * Create a player based on the member with an empty deck.
     *
     * @param member the member who is playing
     * @since 17.12.23
     */
    public PlayerModel(final MemberModel member) {
        this.member = member;
        this.deck = new ArrayList<>();
    }

    /**
     * Return the member who is playing.
     *
     * @return the member who is playing
     * @since 17.12.23
     */
    public MemberModel getMember() {
        return this.member;
    }

    /**
     * Set the member who is playing.
     *
     * @param member the member who is playing
     * @since 17.12.23
     */
    public void setMember(final MemberModel member) {
        this.member = member;
    }

    /**
     * Return the game the player is playing.
     *
     * @return the game the player is playing
     * @since 17.12.23
     */
    public GameModel getGame() {
        return this.game;
    }

    /**
     * Return the player's deck.
     *
     * @return the player's deck
     * @since 17.12.23
     */
    public List<CardInDeckModel> getDeck() {
        return this.deck;
    }

    /**
     * Set the player's deck.
     *
     * @param deck the player's deck
     * @since 17.12.23
     */
    public void setDeck(final List<CardModel> deck) {
        final List<CardInDeckModel> cards = new ArrayList<>();
        for (final CardModel card : deck) {
            cards.add(new CardInDeckModel(this, card));
        }
        this.deck = cards;
    }

    /**
     * Get the player's score based on the number of owned cards (in deck + on board).
     *
     * @return the player's score
     * @since 17.12.26
     */
    public int getScore() {
        int score = this.deck.size();

        for (final CaseModel caseModel : this.game.getBoard().getCases()) {
            if (caseModel.getCardOnCase() != null && caseModel.getCardOnCase().getPlayer().getUid() == this.uid) {
                score++;
            }
        }

        return score;
    }
}
